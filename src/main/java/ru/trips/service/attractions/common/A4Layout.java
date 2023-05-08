package ru.trips.service.attractions.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.PrintSetup;

/**
 * POJO класс для хранения информации об разметке для экспорта списков.
 * Формат А4, портретная ориентация для отчетов карточек.
 *
 * @author AMitrofanov
 */
@Setter
@Getter
@JsonSerialize
@JsonDeserialize
public class A4Layout extends Layout {

    /**
     * Формат листа печати.
     */
    protected short paperSize = PrintSetup.A4_PAPERSIZE;

    /**
     * Флаг альбомной ориентации страницы при печати.
     */
    protected boolean landscape = false;

    /**
     * Ширина листа, в которую вписываем данные для печати.
     */
    protected int formatPageWidth = 22000;
}
