package ru.trips.service.attractions.service.excursion.reports;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.trips.contracts.transfer.domain.attractions.ExcursionFilter;
import ru.trips.contracts.transfer.domain.attractions.ExcursionParams;
import ru.trips.contracts.transfer.domain.attractions.ExcursionTo;
import ru.trips.contracts.transfer.domain.reports.Column;
import ru.trips.contracts.transfer.domain.reports.ExportColumn;
import ru.trips.contracts.transfer.domain.reports.excursion.ExcursionReportForm;
import ru.trips.service.attractions.common.A3Layout;
import ru.trips.service.attractions.common.A4Layout;
import ru.trips.service.attractions.common.ExportConfig;
import ru.trips.service.attractions.common.ExportProvider;
import ru.trips.service.attractions.common.ExportService;
import ru.trips.service.attractions.common.Layout;
import ru.trips.service.attractions.common.MergeConfig;
import ru.trips.service.attractions.service.excursion.ExcursionExportConverter;
import ru.trips.service.attractions.service.excursion.ExcursionGetByIdUseCase;
import ru.trips.service.attractions.service.excursion.ExcursionSearchUseCase;
import ru.trips.service.attractions.service.excursion.data.ExcursionExportData;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * xlsx отчёт для экскурсий.
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class ExcursionXlsxReportUseCase {

    private final ExportService exportService;
    private final ExcursionGetByIdUseCase getByIdUseCase;
    private final ExcursionSearchUseCase excursionSearchUseCase;
    private final ExcursionExportConverter exportConverter;

    private static final String CARD_TITLE = "Обзор экскурсий";

    /**
     * Получение файла экспорта списка.
     *
     * @param form     - параметры включаемых данных, а также порядок колонок в файле.
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     */
    @Transactional(readOnly = true)
    public void exec(ExcursionReportForm form,
                     HttpServletRequest request, HttpServletResponse response) {
        var reportForm = (ExcursionReportForm) form;
        List<ExcursionTo> result = new ArrayList<>();

        if (reportForm == null) {
            log.info("reportForm не должна быть null!");
        }

        Layout layout;
        if (reportForm.getId() != null) {
            layout = new A4Layout();
            var reportData =
                exportConverter.convert(getByIdUseCase.exec(reportForm.getId()), reportForm);
            List<ExportColumn> columns = List.of(
                new ExportColumn(new Column("name", "", 30)),
                new ExportColumn(new Column("value", "", 70))
            );

            List<ExportConfig> exportConfigs = List.of(new ExportConfig(reportForm.getTitle(),
                CARD_TITLE, new ArrayList<>(columns), reportForm.getId() != null, layout,
                reportForm.getTimeOffset()));
            List<ExportProvider> exportProviders = List.of(new ExportProvider(reportData.getRecords()));

            exportService.createExport(new MergeConfig(), exportConfigs, exportProviders, request, response);

        } else {
            ExcursionParams params = new ExcursionParams();
            params.setFilter(reportForm.getFilter() == null
                ? new ExcursionFilter() : reportForm.getFilter());
            params.setSorting(reportForm.getSorting());

            result.addAll(excursionSearchUseCase.exec(params).getContent());
            layout = new A3Layout();

            var columns = exportConverter.convert(reportForm.getColumns(),
                ExcursionExportData.class);
            var data = exportConverter.convert(result);

            List<ExportConfig> exportConfigs = List.of(new ExportConfig(reportForm.getTitle(),
                reportForm.getSubTitle(), columns, reportForm.getId() != null, layout,
                reportForm.getTimeOffset()));

            List<ExportProvider> exportProviders = List.of(new ExportProvider(data));

            exportService.createExport(new MergeConfig(), exportConfigs, exportProviders, request, response);
        }
    }
}
