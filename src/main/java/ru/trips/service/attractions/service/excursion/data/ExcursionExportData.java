package ru.trips.service.attractions.service.excursion.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.trips.service.attractions.domain.entity.AttractionEntity;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Данные для выгрузки.
 */
@Setter
@Getter
@NoArgsConstructor
public class ExcursionExportData {

    /**
     * Номер строки.
     */
    private String recordNumber;

    /**
     * Описание.
     */
    private String description;

    /**
     * Цена за экскурсию.
     */
    private String price;

    /**
     * Дополнительная информация.
     */
    private String information;
}
