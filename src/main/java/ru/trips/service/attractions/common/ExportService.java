package ru.trips.service.attractions.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Сервис для экспорта данных из списков.
 */
@Slf4j
@Service
public class ExportService {

    private final ExcelReportBuilder excelReportBuilder;
    private final Semaphore semaphore;
    private final int parallelCount;

    /**
     * Конструктор.
     * <p/>
     *
     * @param excelReportBuilder генератор Excel-представления
     */
    public ExportService(ExcelReportBuilder excelReportBuilder) {
        this.excelReportBuilder = excelReportBuilder;

        // ручной барьер - сколько процесить параллельно выгрузок
        this.parallelCount = 3;
        semaphore = new Semaphore(parallelCount);

    }

    /**
     * Запуск построения отчета.
     *
     * @param mergeConfig     общая конфигурация файла отчёта
     * @param exportConfigs   ExportConfig
     * @param exportProviders ExportProvider
     * @param request         HttpServletRequest
     * @param response        HttpServletResponse
     */
    public void createExport(final MergeConfig mergeConfig,
                             final List<ExportConfig> exportConfigs,
                             final List<ExportProvider> exportProviders,
                             final HttpServletRequest request,
                             final HttpServletResponse response) {

        LocalDateTime start = LocalDateTime.now();

        final Map<String, Object> map = new HashMap<>();

        for (var exportConfig : exportConfigs) {
            exportConfig.deleteHiddenColumns();
        }

        map.put("mergeConfig", mergeConfig);
        map.put("exportConfigs", exportConfigs);
        map.put("exportProviders", exportProviders);

        try {
            logInfo();

            if (parallelCount > 0) {
                semaphore.acquire();
            }

            excelReportBuilder.render(map, request, response);
            log.info(
                "Preparing EXCEL document ({}), elapsed time {} second(s)", mergeConfig.getFileNamePrefix(),
                Duration.between(start, LocalDateTime.now()).getSeconds());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            if (parallelCount > 0) {
                semaphore.release();
            }
        }
    }

    private void logInfo() {
        if (parallelCount <= 0) {
            log.info("Очередь выгрузок не используется");
        } else {
            if (semaphore.availablePermits() < 1) {
                log.info("Выгрузка поставлена в очередь, т.к. превышено число одновременных выгрузок - {}",
                    parallelCount);
                log.info("В работе {} выгрузок(ки), ожидают очереди приблизительно - {} выгрузок(ки)",
                    parallelCount, semaphore.getQueueLength() + 1);
            } else {
                log.info("Очередь свободна, выгрузка начинается немедленно");
            }
        }
    }
}
