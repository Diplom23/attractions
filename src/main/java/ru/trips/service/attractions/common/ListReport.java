package ru.trips.service.attractions.common;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import ru.trips.contracts.transfer.domain.reports.CommonUtils;
import ru.trips.contracts.transfer.domain.reports.ExportColumn;

/**
 * Класс для наполнения данными классических отчетов.
 */
public class ListReport extends BaseReport {

    /**
     * Заполняет строки таблицы данными.
     *
     * @param dataProvider - провайдер данных.
     * @param exportConfig - конфигурация для таблицы.
     * @param sheet        - Excel таблица.
     */
    public void createListReport(ExportProvider dataProvider, ExportConfig exportConfig, XSSFSheet sheet) {

        // заполняется титул
        fillTitle(exportConfig.getListName(), sheet, exportConfig.getColumns().size() - 1, 0, 0,
            excelStyles.createTitleStyle(sheet.getWorkbook()));

        // по умолчанию считается, что сабтитул отсутствует
        int offsetRow = 1;
        String subListName = exportConfig.getSubTitle();

        if (subListName != null && !subListName.isEmpty()) {
            offsetRow = 2;
            // заполняется сабтитул
            fillTitle(exportConfig.getSubTitle(), sheet, exportConfig.getColumns().size() - 1, 1, 0,
                excelStyles.createSubTitleStyle(sheet.getWorkbook()));
        }
        // заполняются заголовки таблицы в одной строке
        fillHeader(exportConfig, sheet, offsetRow);
        // заполняются данные
        int rowNum = fillData(dataProvider, exportConfig, sheet, offsetRow);
        // заполняется нижний колонтитул
        fillFooter(sheet, ++rowNum, exportConfig.getColumns().size() - 1, false,
            exportConfig.getTimeOffset());
    }

    private int fillData(ExportProvider dataProvider, ExportConfig exportConfig, XSSFSheet sheet, int rowNumber) {
        Iterable<ExportRow> exportRows = dataProvider.getMoreRows();
        int rowNum = rowNumber;
        while (exportRows.iterator().hasNext()) {
            for (ExportRow exportRow : exportRows) {
                rowNum++;
                final Row row = sheet.createRow(rowNum);

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
        return rowNum;
    }
}
