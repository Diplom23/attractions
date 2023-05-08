package ru.trips.service.attractions.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.trips.contracts.transfer.domain.reports.CommonUtils;
import ru.trips.contracts.transfer.domain.reports.ExportColumn;

import java.util.Collections;
import java.util.List;

/**
 * Базовый класс для отчетов, содержит общие базовые методы.
 */
@Slf4j
public abstract class BaseReport {

    /**
     * Набор стилей для таблиц.
     */
    protected final ExcelStyles excelStyles = new ExcelStyles();

    /**
     * "Дефолтный" набор колонок для отчета.
     * Для переопределения в наследниках.
     */
    public List<ExportColumn> columns = Collections.emptyList();

    /**
     * Базовая установка стилей заголовка и ячеек.
     *
     * @param exportConfigs - набор {@link ExportConfig}
     * @param workbook      - {@link XSSFWorkbook}
     */
    public static void setupStyles(List<ExportConfig> exportConfigs, XSSFWorkbook workbook) {
        CellStyle headerStyle = ExcelStyles.createHeaderStyle(workbook);
        CellStyle plainStyle = ExcelStyles.createCellStyle(workbook);
        for (var exportConfig : exportConfigs) {
            // сначала заполним дефолтными стилями
            for (ExportColumn column : exportConfig.getColumns()) {
                column.setHeaderStyle(headerStyle);
                column.setStyle(plainStyle);
            }
        }
    }

    /**
     * Заполнение заголовка.
     *
     * @param titleName - текст заголовка
     * @param sheet     - таблица
     * @param lastCol   - последняя колонка таблицы
     * @param rowNumber - номер строки в которой находится заголовок
     * @param rowHeight - новая высота строки заголовка, высоте не будет меняться если передать 0.
     * @param style     - стиль заголовка
     */
    protected void fillTitle(String titleName, XSSFSheet sheet, int lastCol, int rowNumber, int rowHeight,
                             CellStyle style) {
        final Row title = sheet.createRow(rowNumber);

        if (rowHeight > 0) {
            title.setHeightInPoints(rowHeight);
        }

        CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNumber, rowNumber, 0, lastCol);
        final Cell titleCell = title.createCell(0);
        // объединять меньше 2 ячеек нельзя, будет exception
        if (lastCol > 0) {
            sheet.addMergedRegion(cellRangeAddress);
        }
        titleCell.setCellValue(titleName);

        CommonUtils.applyFullBorderToRegion(sheet, cellRangeAddress);
        titleCell.setCellStyle(style);

    }

    /**
     * Заполнение заголовка для классических отчётов и множественных.
     *
     * @param exportConfig - конфигурация отчёта.
     * @param sheet        - excel таблица.
     * @param rowNumber    - номер строки в которой будет заголовок.
     */
    protected void fillHeader(ExportConfig exportConfig, XSSFSheet sheet, int rowNumber) {
        final Row header = sheet.createRow(rowNumber);
        for (int i = 0; i < exportConfig.getColumns().size(); i++) {
            final ExportColumn column = exportConfig.getColumns().get(i);
            final Cell cell = header.createCell(i);
            cell.setCellValue(column.getTitle());
            cell.setCellStyle((CellStyle) column.getHeaderStyle());
        }
    }

    /**
     * Заполнение нижнего колонтитула.
     *
     * @param sheet      - excel таблица.
     * @param lastRow    - последняя строка.
     * @param lastCol    - последняя колонка.
     * @param isEmpty    - факт наличия текста в нижнем колонтитуле.
     * @param timeOffset - сколько минут сдвиг от часового пояса МСК.
     */
    protected void fillFooter(XSSFSheet sheet, int lastRow, int lastCol, boolean isEmpty, Integer timeOffset) {
        final Row footer = sheet.createRow(lastRow);
        final Cell footerCell = footer.createCell(0);
        if (lastCol < 0) {
            log.info("Были переданы несуществующие поля! Введите корректный набор полей!");
        }
        CellRangeAddress cellRangeAddress = new CellRangeAddress(lastRow, lastRow, 0, lastCol);
        // объединять меньше 2 ячеек нельзя, будет exception
        if (lastCol > 0) {
            sheet.addMergedRegion(cellRangeAddress);
        }
        if (!isEmpty) {
            footerCell.setCellValue(CommonUtils.getFooterString(timeOffset));
        }

        // необходимо для формирования границ объединенных ячеек
        CommonUtils.applyFullBorderToRegion(sheet, cellRangeAddress);
        // применяется итоговый стиль для 1-ой ячейки в которой содержится текст
        var footerStyle = excelStyles.createFooterStyle(sheet.getWorkbook());
        footerCell.setCellStyle(footerStyle);
    }
}
