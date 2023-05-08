package ru.trips.service.attractions.common;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import ru.trips.contracts.transfer.domain.reports.CommonUtils;
import ru.trips.contracts.transfer.domain.reports.ExportColumn;


/**
 * Класс для наполнения данными отчёта карточки.
 */
public class CardReport extends BaseReport {

    /**
     * Заполняет строки таблицы данными, когда заголовки расположены в колонку.
     *
     * @param dataProvider провайдер данных
     * @param exportConfig конфигурация для таблицы
     * @param sheet        Excel таблица
     * @param cardTitle    заголовок
     */
    public void createReport(ExportProvider dataProvider, ExportConfig exportConfig, XSSFSheet sheet,
                             String cardTitle) {

        /*// заполняются заголовки таблицы в первой колонке
        fillHeader(exportConfig, sheet);*/
        // заполняется заголовок ГАС Выборы
        fillTitle(cardTitle, sheet, exportConfig.getColumns().size() - 1,
            0, 42, excelStyles.createCardTitleStyle(sheet.getWorkbook()));
        // заполняется титул таблицы
        fillTitle(exportConfig.getListName(), sheet, exportConfig.getColumns().size() - 1,
            1, 30, excelStyles.createTitleStyle(sheet.getWorkbook()));

        // заполняются данные
        int countElements = fillData(dataProvider, exportConfig, sheet);

        // заполняется нижний колонтитул
        fillFooter(sheet, countElements + 1, exportConfig.getTimeOffset());
    }

    private int fillData(ExportProvider dataProvider, ExportConfig exportConfig, XSSFSheet sheet) {
        Iterable<ExportRow> exportRows = dataProvider.getMoreRows();
        int rowNumber = 1;
        while (exportRows.iterator().hasNext()) {
            for (ExportRow exportRow : exportRows) {
                rowNumber++;
                final Row row = sheet.createRow(rowNumber);
                for (int i = 0; i < exportConfig.getColumns().size(); i++) {
                    final ExportColumn column = exportConfig.getColumns().get(i);
                    final Cell cell = row.createCell(i);
                    cell.setCellStyle((CellStyle) column.getStyle());
                    Object value = exportRow.getValueOfColumn(column);
                    CommonUtils.setCallValue(sheet, value, cell);
                }
            }
            exportRows = dataProvider.getMoreRows();
        }
        return rowNumber;
    }

    /**
     * Заполнение нижнего колонтитула.
     *
     * @param sheet      - excel таблица.
     * @param lastRow    - последняя строка.
     * @param timeOffset - сколько минут сдвиг от часового пояса МСК.
     */
    protected void fillFooter(XSSFSheet sheet, int lastRow, Integer timeOffset) {
        final Row footer = sheet.createRow(lastRow);
        final Cell footerCell = footer.createCell(0);

        CellRangeAddress cellRangeAddress = new CellRangeAddress(lastRow, lastRow, 0, 1);
        // объединять меньше 2 ячеек нельзя, будет exception
        sheet.addMergedRegion(cellRangeAddress);

        footerCell.setCellValue(CommonUtils.getFooterString(timeOffset));

        // необходимо для формирования границ объединенных ячеек
        CommonUtils.applyFullBorderToRegion(sheet, cellRangeAddress);
        // применяется итоговый стиль для 1-ой ячейки в которой содержится текст
        var footerStyle = excelStyles.createFooterStyle(sheet.getWorkbook());
        footerCell.setCellStyle(footerStyle);
    }
}
