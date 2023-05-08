package ru.trips.service.attractions.common;

import lombok.extern.slf4j.Slf4j;
import ru.trips.contracts.transfer.domain.reports.Column;
import ru.trips.contracts.transfer.domain.reports.ExportColumn;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Базовый конвертор для экспорта данных в отчёты.
 */
@Slf4j
public abstract class BaseExportConverter {

    /**
     * Конвертация данных, полученных с клиента в объекты ExportColumn.
     *
     * @param columns     - список столбцов определяющий их порядок в отчёте.
     * @param exportClass - класс для экспорта.
     * @return список колонок экспорта.
     */
    public List<ExportColumn> convert(List<Column> columns, Class<?> exportClass) {
        // получаем набор доступных полей родителей и самого экспорт класса
        List<Field> result = Arrays.asList(exportClass.getDeclaredFields());

        Class<?> superClass = exportClass.getSuperclass();
        if (superClass != null) {
            List<Field> declaredSuperFields = Arrays.asList(superClass.getDeclaredFields());
            result = Stream.concat(result.stream(), declaredSuperFields.stream()).collect(Collectors.toList());

            Class<?> parentSuperClass = superClass.getSuperclass();
            if (parentSuperClass != null) {
                result = Stream.concat(result.stream(), Arrays.stream(parentSuperClass.getDeclaredFields()))
                    .collect(Collectors.toList());
            }
        }

        // получаем набор имён доступных полей класса
        List<String> nameFields = result.stream().map(Field::getName).collect(Collectors.toList());
        if (columns == null) {
            log.info("Список колонок для формирования отчёта не должен быть null!");
        }
        return columns.stream().filter(Objects::nonNull).filter(col -> nameFields.contains(col.getField()))
            .map(ExportColumn::new).collect(Collectors.toList());
    }
}
