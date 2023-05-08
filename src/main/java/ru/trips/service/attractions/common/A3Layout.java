package ru.trips.service.attractions.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.PrintSetup;

/**
 * POJO класс для хранения информации об разметке для экспорта списков.
 * Формат А3, альбомная ориентация для отчетов списков.
 *
 * @author AMitrofanov
 */
@Setter
@Getter
@JsonSerialize
@JsonDeserialize
public class A3Layout extends Layout {

    /**
     * Формат листа печати.
     */
    protected short paperSize = PrintSetup.A3_PAPERSIZE;

    /**
     * Флаг альбомной ориентации страницы при печати.
     */
    protected boolean landscape = true;

    /**
     * Ширина листа, в которую вписываем данные для печати.
     */
    protected int formatPageWidth = 46000;
}
