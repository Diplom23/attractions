package ru.trips.service.attractions.service.excursion;

import liquibase.pro.packaged.S;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.trips.contracts.transfer.domain.attractions.ExcursionTo;
import ru.trips.contracts.transfer.domain.hotels.apartments.ApartmentsTo;
import ru.trips.contracts.transfer.domain.reports.CardReportData;
import ru.trips.contracts.transfer.domain.reports.CardReportRecord;
import ru.trips.contracts.transfer.domain.reports.CommonUtils;
import ru.trips.contracts.transfer.domain.reports.apartments.ApartmentsReportForm;
import ru.trips.contracts.transfer.domain.reports.excursion.ExcursionReportForm;
import ru.trips.service.attractions.common.BaseExportConverter;
import ru.trips.service.attractions.domain.entity.AttractionEntity;
import ru.trips.service.attractions.domain.entity.ExcursionEntity;
import ru.trips.service.attractions.domain.repository.ExcursionRepository;
import ru.trips.service.attractions.service.excursion.data.ExcursionExportData;

import java.util.ArrayList;
import java.util.List;

/**
 * Конвертер объекта для отчета.
 */
@Component
@RequiredArgsConstructor
public class ExcursionExportConverter extends BaseExportConverter {
    private final ModelMapper modelMapper = new ModelMapper();
    private final ExcursionRepository excursionRepository;

    /**
     * Конвертация данных в объекты ApartmentsExportData.
     *
     * @param data - набор сущностей.
     * @return список сущностей для отчёта.
     */
    public List<ExcursionExportData> convert(List<ExcursionTo> data) {
        List<ExcursionExportData> exportData = new ArrayList<>();
        int recordNumber = 1;
        for (var to : data) {
            var result = modelMapper.map(to, ExcursionExportData.class);
            result.setDescription(to.getDescription());
            result.setPrice(to.getPrice().toString());
            result.setInformation(to.getInformation());
            result.setRecordNumber(String.valueOf(recordNumber++));
            exportData.add(result);
        }
        return exportData;
    }

    /**
     * Конвертация данных в объекты CardReportData.
     *
     * @param excursionTo - сущность.
     * @param reportForm   - параметры отчета.
     * @return список сущностей для отчёта.
     */
    public CardReportData convert(ExcursionTo excursionTo,
                                  ExcursionReportForm reportForm) {
        var reportData = new CardReportData();
        reportData.setReportType("карточка");
        reportData.setFooter(CommonUtils.getFooterString(reportForm.getTimeOffset()));
        reportForm.setTitle(header(excursionTo).getName());

        reportData.getRecords().add(new CardReportRecord(
            "Об экскурсии:",
            excursionTo.getDescription() != null
                ? excursionTo.getDescription() : ""
        ));

        reportData.getRecords().add(new CardReportRecord(
            "Цена:",
            excursionTo.getPrice() != null
                ? excursionTo.getPrice().toString() : ""
        ));

        reportData.getRecords().add(new CardReportRecord(
            "Важная информация:",
            excursionTo.getInformation() != null
                ? excursionTo.getInformation() : ""
        ));

        return reportData;
    }

    private AttractionEntity header(ExcursionTo excursion) {
        ExcursionEntity entity = excursionRepository.getById(excursion.getId());
        return entity.getAttraction();
    }
}
