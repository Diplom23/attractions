package ru.trips.service.attractions.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.PrintSetup;

/**
 * Класс для хранения информации об разметке для экспорта списков.
 */
@Setter
@Getter
@JsonSerialize
@JsonDeserialize
public class Layout {

    /**
     * Формат листа печати.
     */
    protected short paperSize = PrintSetup.A4_PAPERSIZE; //Возможные значения: A2, A3, A4, A5, Letter

    /**
     * Флаг альбомной ориентации страницы при печати.
     */
    protected boolean landscape = false;

    private String pageMargin = "20mm";

    /**
     * Ширина листа, в которую вписываем данные для печати.
     */
    protected int formatPageWidth = 22000;

    /**
     * Количество полей в документе(право\лево верх\низ header\footer).
     */
    public static final short MARGINS_COUNT = 6;

    /**
     * настройка печати из шаблона.
     */
    private Object templatePrintSetup;

    private boolean templateFitToPage;

    private double[] templateMargins = new double[] {0, 0, 0, 0, 0, 0};

}
