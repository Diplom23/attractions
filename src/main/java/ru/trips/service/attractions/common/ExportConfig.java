package ru.trips.service.attractions.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import ru.trips.contracts.transfer.domain.reports.ExportColumn;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Объект для хранения конфигов экспорта списка.
 */
@Setter
@Getter
@JsonSerialize
@JsonDeserialize
public class ExportConfig {

    /**
     * Наименование формата выходного файла.
     */
    private String format;

    /**
     * Признак горизонтальной выгрузки данных (для карточки).
     */
    private boolean horizontal;

    /**
     * Наименование выходного файла без расширения.
     */
    private String fileName;

    /**
     * Наименование списка.
     */
    private String listName;

    /**
     * Наименование сабтитула.
     */
    private String subTitle;

    /**
     * Наименование титула для карточки.
     */
    private String cardTitle;

    /**
     * смещение часового пояса клиента.
     */
    private Integer timeOffset;

    /**
     * Префикс для формирования файла, если не задано поле fileName.
     * Имя выходного файла формируется из префикса, текущего времени
     * и расширения как {fileNamePrefix}-{yyyyMMdd-HHmmss}.{ext}
     */
    private String fileNamePrefix = "Export";

    /**
     * Параметры выводы страницы.
     */
    private Layout layout = new Layout();

    /**
     * Файк наличия шаблона для файла экспорта.
     */
    private boolean template;

    /**
     * Описание колонок.
     */
    private List<ExportColumn> columns = new ArrayList<>();

    /**
     * Конструктор.
     *
     * @param listName наименование списка
     * @param subTitle наименование сабтитула
     * @param columns  колонки экспорта
     */
    public ExportConfig(String listName, String subTitle, List<ExportColumn> columns) {
        this.columns = columns;
        this.listName = listName;
        this.subTitle = subTitle;
    }

    /**
     * Конструктор.
     *
     * @param listName   наименование списка
     * @param cardTitle  наименование титула
     * @param columns    колонки экспорта
     * @param horizontal признак построения по горизонтали (для карточки)
     * @param layout     шаблон разметки страницы
     * @param timeOffset шаблон разметки страницы
     */
    public ExportConfig(String listName, String cardTitle,
                        List<ExportColumn> columns, boolean horizontal, Layout layout, Integer timeOffset) {
        this.columns = columns;
        this.listName = listName;
        this.cardTitle = cardTitle;
        this.horizontal = horizontal;
        if (layout != null) {
            this.layout = layout;
        }
        this.timeOffset = timeOffset;
    }

    private List<Map<String, Object>> rows;

    /**
     * Формирует имя файла из настроек экспорта.
     *
     * @param fileExt рассширения файла
     * @return имя файла с рассширением
     */
    public String calculateFileNameFromConfig(String fileExt) {
        if (getFileName() != null && !getFileName().isEmpty()) {
            return getFileName() + fileExt;
        } else {
            final String date = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            return getFileNamePrefix() + "-" + date + fileExt;
        }
    }

    /**
     * Формирует имя шалона из настроек экспорта.
     *
     * @param fileExt рассширения файла
     * @return имя файла с рассширением
     */
    public String getTemplateName(String fileExt) {
        return getFileNamePrefix() + fileExt;
    }

    /**
     * Удалить скрытые столбцы.
     */
    public void deleteHiddenColumns() {
        for (int i = 0; i < getColumns().size(); i++) {
            final ExportColumn column = getColumns().get(i);
            if (column.isHidden()) {
                getColumns().set(i, null);
            }
        }
        getColumns().removeAll(Collections.singleton(null));
    }

}
