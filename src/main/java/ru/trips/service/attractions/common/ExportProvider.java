package ru.trips.service.attractions.common;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Базовая реализация поставщика данных для экспорта.
 * Разбирает строку format из клиентские настроек, форматирование
 * примитивных полей делегирует ExportFormatter.
 */
@Slf4j
@Getter
public class ExportProvider {

    private final List<?> data;
    private boolean done = false;

    /**
     * Конструктор.
     *
     * @param data данные
     */
    public ExportProvider(List<?> data) {
        this.data = data;
    }

    /**
     * Функция получения набора данных.
     *
     * @return набор свойств
     */
    public Iterable<ExportRow> getMoreRows() {

        if (done) {
            return Collections.emptyList();
        }
        List<ExportRow> result = new ArrayList<>();
        done = true;
        for (var dataObject : data) {
            result.add(
                column -> {
                    BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(dataObject);
                    if (column.getFormat() == null) {
                        try {
                            return formatPrimitive(
                                beanWrapper.getPropertyValue(column.getPropName()), column.getPropName());
                        } catch (Exception ex) {
                            log.error("Column read error {}", column.getPropName(), ex);
                            return column.getPropName();
                        }
                    }
                    String format = column.getFormat();
                    Set<String> properties = getPropertyPlaceholders(column.getFormat());
                    for (String property : properties) {
                        try {
                            format = format.replace(
                                "{" + property + "}",
                                formatPrimitive(beanWrapper.getPropertyValue(property), property).toString());
                        } catch (Exception ex) {
                            format = format.replace(
                                "{" + property + "}", "");
                            log.debug("Preloads error {}", property, ex);
                        }
                    }
                    return format
                        .replace("<b>", "")
                        .replace("</b>", "")
                        .replace("<i>", "")
                        .replace("</i>", "");
                });
        }
        return result;
    }

    private Object formatPrimitive(Object value, String propName) {
        return value == null ? "" : value;

    }

    // валидный java-идентификатор или точка в фигурных скобках
    private static final Pattern PATTERN = Pattern.compile("\\{[\\w$.]*}");

    /**
     * Достает все плейсхолдеры свойств из строки формата вида {prop1.prop2}
     *
     * @param format строка формата
     * @return набор свойств
     */
    public static Set<String> getPropertyPlaceholders(String format) {

        Set<String> result = new HashSet<>();
        if (format == null) {
            return result;
        }
        Matcher matcher = PATTERN.matcher(format);

        List<Integer> starts = new ArrayList<>();
        List<Integer> ends = new ArrayList<>();
        while (matcher.find()) {
            starts.add(matcher.start());
            ends.add(matcher.end());
        }

        for (int i = starts.size() - 1; i >= 0; i--) {
            result.add(format.substring(starts.get(i) + 1, ends.get(i) - 1));
        }

        return result;
    }

}
