package ru.trips.service.attractions.common;

import ru.trips.contracts.transfer.domain.reports.ExportColumn;

/**
 * Строка для экспорта.
 */
public interface ExportRow {

    /**
     * Получить строковое представление данной колонки.
     *
     * @param column конфигруаций колонки.
     * @return строка для экспорта.
     */
    Object getValueOfColumn(ExportColumn column);

}
