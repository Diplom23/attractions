package ru.trips.service.attractions.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.trips.contracts.transfer.PageData;
import ru.trips.contracts.transfer.domain.attractions.ExcursionParams;
import ru.trips.contracts.transfer.domain.attractions.ExcursionTo;
import ru.trips.contracts.transfer.domain.reports.excursion.ExcursionReportForm;
import ru.trips.contracts.transfer.domain.reports.hotel.HotelReportForm;
import ru.trips.service.attractions.service.excursion.ExcursionGetByIdUseCase;
import ru.trips.service.attractions.service.excursion.ExcursionSearchUseCase;
import ru.trips.service.attractions.service.excursion.reports.ExcursionHtmlReportUseCase;
import ru.trips.service.attractions.service.excursion.reports.ExcursionXlsxReportUseCase;

import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Контроллер для взаимодействия с экскурсиями.
 */
@RestController
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/excursion")
public class ExcursionController {

    private final ExcursionSearchUseCase searchUseCase;
    private final ExcursionGetByIdUseCase byIdUseCase;
    private final ExcursionHtmlReportUseCase htmlReportUseCase;
    private final ExcursionXlsxReportUseCase xlsxReportUseCase;

    /**
     * Поиск по id.
     *
     * @param id id
     * @return сущность
     */
    @SneakyThrows
    @GetMapping("/{id}")
    public ResponseEntity<ExcursionTo> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(byIdUseCase.exec(id));
    }

    /**
     * Поиск.
     *
     * @param params параметры запроса
     * @return данные.
     */
    @SneakyThrows
    @PostMapping("/search")
    public ResponseEntity<PageData<ExcursionTo>> searchByParams(
        @RequestBody ExcursionParams params) {
        return ResponseEntity.ok(searchUseCase.exec(params));
    }

    /**
     * Получение html отчёта "Список экскурсий".
     *
     * @param reportForm - набор наименования колонок отчёта, а также фильтр
     * @return html отчёт в виде массива байт
     */
    @PostMapping("/html-report")
    public ResponseEntity<ByteArrayResource> getExcursionHtmlReport(@RequestBody ExcursionReportForm reportForm) {
        return ResponseEntity.ok(htmlReportUseCase.exec(reportForm));
    }

    /**
     * Получение файла.xlsx отчета "Список экскурсий".
     *
     * @param reportForm - набор наименования колонок отчёта
     * @param request    HttpServletRequest
     * @param response   HttpServletResponse
     */
    @PostMapping("/xlsx-report")
    public void getExcursionXlsxReport(@RequestBody ExcursionReportForm reportForm,
                                   final HttpServletRequest request,
                                   final HttpServletResponse response) {
        xlsxReportUseCase.exec(reportForm, request, response);
    }
}
