package ru.trips.service.attractions.service.excursion.reports;

import htmlflow.DynamicHtml;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.trips.contracts.transfer.Pagination;
import ru.trips.contracts.transfer.domain.attractions.ExcursionFilter;
import ru.trips.contracts.transfer.domain.attractions.ExcursionParams;
import ru.trips.contracts.transfer.domain.attractions.ExcursionTo;
import ru.trips.contracts.transfer.domain.reports.HtmlParameters;
import ru.trips.contracts.transfer.domain.reports.HtmlReportTemplate;
import ru.trips.contracts.transfer.domain.reports.excursion.ExcursionReportForm;
import ru.trips.service.attractions.service.excursion.ExcursionExportConverter;
import ru.trips.service.attractions.service.excursion.ExcursionSearchUseCase;

import java.util.ArrayList;
import java.util.List;

/**
 * html отчёт для экскурсий.
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class ExcursionHtmlReportUseCase {

    private final ExcursionSearchUseCase searchUseCase;
    private final ExcursionExportConverter exportConverter;

    /**
     * Получение Html отчёта.
     *
     * @param form - параметры включаемых данных, а также порядок колонок в отчёте.
     * @return html отчёт.
     */
    @Transactional(readOnly = true)
    public ByteArrayResource exec(ExcursionReportForm form) {
        var reportForm = (ExcursionReportForm) form;
        List<ExcursionTo> result = new ArrayList<>();

        if (reportForm == null) {
            log.info("ExcursionReportForm не должна быть null!");
        }

        ExcursionParams params = new ExcursionParams();
        Pagination pagination = Pagination.of(0, 100);
        params.setFilter(reportForm.getFilter() == null ? new ExcursionFilter() : reportForm.getFilter());
        params.setPagination(pagination);
        params.setSorting(reportForm.getSorting());

        var pageData = searchUseCase.exec(params);
        int totalPages = pageData.getMetaData().getTotalPages();
        while (pagination.getPageNo() < totalPages) {
            pagination.setPageNo(pagination.getPageNo() + 1);

            result.addAll(pageData.getContent());

            pageData = searchUseCase.exec(params);
        }

        var columns = exportConverter.convert(reportForm.getColumns(), ExcursionExportConverter.class);
        var data = exportConverter.convert(result);

        return new ByteArrayResource(DynamicHtml.view(HtmlReportTemplate::htmlTemplate)
            .render(new HtmlParameters(reportForm.getTitle(), reportForm.getSubTitle(), columns,
                data, reportForm.getTimeOffset())).getBytes());
    }
}
