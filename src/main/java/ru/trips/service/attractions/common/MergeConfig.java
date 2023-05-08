package ru.trips.service.attractions.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

/**
 * Объект для хранения конфигурации нескольких xlsx таблиц.
 */
@Setter
@Getter
@JsonSerialize
@JsonDeserialize
public class MergeConfig {

    /**
     * Общий заголовок.
     */
    private String header;

    /**
     * Параметры выводы страницы.
     */
    private Layout layout = new Layout();

    /**
     * Префикс для формирования файла, если не задано поле fileName.
     * Имя выходного файла формируется из префикса, текущего времени
     * и расширения как {fileNamePrefix}-{yyyyMMdd-HHmmss}.{ext}
     */
    private String fileNamePrefix = "Export";

    /**
     * Факт наличия шаблона для файла экспорта.
     */
    private boolean template;

}
