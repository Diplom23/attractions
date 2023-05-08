package ru.trips.service.attractions.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;
import ru.trips.contracts.transfer.domain.reports.CommonUtils;
import ru.trips.contracts.transfer.domain.reports.ExportColumn;
import ru.trips.contracts.transfer.domain.reports.ReportData;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Компонент отвечающий за генерацию Excel представления экспорта списка.
 */
@Slf4j
@Component
public class ExcelReportBuilder extends AbstractXlsxView {

    private final CardReport cardReport = new CardReport();
    private final ListReport listReport = new ListReport();

    /**
     * Количество MARGINS.
     */
    public static final short MARGINS_COUNT = 6;
    private static final String CARD_TITLE = "Обзор экскурсий";
    private final ResourceLoader resourceLoader;

    /**
     * Конструктор.
     *
     * @param resourceLoader resourceLoader
     */
    public ExcelReportBuilder(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    protected Workbook createWorkbook(Map<String, Object> model, HttpServletRequest request) {

        //Workbook с поддержкой стримминга, те будет писать на диск пачку по 500 строк
        return new SXSSFWorkbook(500);
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
                                      HttpServletResponse response) {
        String cardTitle = CARD_TITLE;

        final MergeConfig mergeConfig = (MergeConfig) model.get("mergeConfig");

        @SuppressWarnings("unchecked") final List<ExportConfig> exportConfigs =
            (List<ExportConfig>) model.get("exportConfigs");
        @SuppressWarnings("unchecked") final List<ExportProvider> exportProviders =
            (List<ExportProvider>) model.get("exportProviders");

        final ReportData reportData = (ReportData) model.get("reportData");

        log.info("Preparing EXCEL document");

        var wb = ((SXSSFWorkbook) workbook).getXSSFWorkbook();

        BaseReport.setupStyles(exportConfigs, wb);
        XSSFSheet sheet = wb.createSheet("sheet-1");

        if (exportConfigs.size() == 1 && exportProviders.size() == 1) {
            ExportConfig exportConfig = exportConfigs.get(0);
            ExportProvider exportProvider = exportProviders.get(0);

            if (exportConfigs.get(0).getCardTitle() != null) {
                cardTitle = exportConfigs.get(0).getCardTitle();
            }
            if (exportConfig.isHorizontal()) {
                cardReport.createReport(exportProvider, exportConfig, sheet, cardTitle);
            } else {
                listReport.createListReport(exportProvider, exportConfig, sheet);
            }
            setupLayout(sheet, exportConfig, cardTitle);
        }

    }

    /**
     * Установка стилей для отчета.
     *
     * @param sheet - страница
     * @param exportConfig конфиги
     * @param cardTitle заголовок
     */
    protected void setupLayout(XSSFSheet sheet, ExportConfig exportConfig, String cardTitle) {

        if (exportConfig.isTemplate()) {
            sheet.setFitToPage(exportConfig.getLayout().isTemplateFitToPage());
            PrintSetup printSetup = sheet.getPrintSetup();
            PrintSetup template = (PrintSetup) exportConfig.getLayout().getTemplatePrintSetup();
            printSetup.setPaperSize(template.getPaperSize());
            printSetup.setLandscape(template.getLandscape());
            printSetup.setFitWidth(template.getFitWidth());
            printSetup.setFitHeight(template.getFitHeight());
            for (short m = 0; m < MARGINS_COUNT; m++) {
                sheet.setMargin(m, exportConfig.getLayout().getTemplateMargins()[m]);
            }

        } else {
            sheet.getPrintSetup().setLandscape(exportConfig.getLayout().isLandscape());
            sheet.getPrintSetup().setPaperSize(exportConfig.getLayout().getPaperSize());
        }

        // первоначальный подсчет ширины колонок для расчёта новой ширины колонок
        int columnWidthSum = 0;
        for (int i = 0; i < exportConfig.getColumns().size(); i++) {
            final ExportColumn column = exportConfig.getColumns().get(i);
            columnWidthSum += column.getWidth();
        }

        sheet.getPrintSetup().setFitWidth((short) 1);

        String title = exportConfig.getListName();
        String subTitle = exportConfig.getSubTitle();

        if (exportConfig.getLayout().isLandscape()) {
            for (int i = 0; i < exportConfig.getColumns().size(); i++) {
                final ExportColumn column = exportConfig.getColumns().get(i);
                sheet.setColumnWidth(i,
                    exportConfig.getLayout().getFormatPageWidth() * column.getWidth() / columnWidthSum);

            }
            // в книжной ориентации выводим карточки с двумя колонками
        } else {
            sheet.setColumnWidth(0, exportConfig.getLayout().getFormatPageWidth() * 3 / 10);
            sheet.setColumnWidth(1, exportConfig.getLayout().getFormatPageWidth() * 7 / 10);

            //для отчетов карточек опустим тайтл до сабтайтла для адекватного расчета параметров.
            title = cardTitle;
            subTitle = exportConfig.getListName();
        }

        // собираем итоговую ширину всех заполненных колонок
        int sumWidthAllColumns = CommonUtils.getWidthAllColumns(sheet);
        CommonUtils.checkingParamsForSetHeight(title, 0, sheet, sumWidthAllColumns, 1.5f);
        CommonUtils.checkingParamsForSetHeight(subTitle, 1, sheet, sumWidthAllColumns, 1.5f);
    }

}
