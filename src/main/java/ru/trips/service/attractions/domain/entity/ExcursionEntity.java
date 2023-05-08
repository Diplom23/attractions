package ru.trips.service.attractions.domain.entity;

import static javax.persistence.ParameterMode.IN;
import static javax.persistence.ParameterMode.INOUT;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import ru.trips.service.attractions.type.CustomJsonBinaryType;

import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedStoredProcedureQueries;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;

/**
 * Сущность "Экскурсия".
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "excursion")
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(
        name = ExcursionEntity.SEARCH,
        procedureName = "excursion_search", parameters = {
        @StoredProcedureParameter(name = ExcursionEntity.PARAMS, mode = IN,
            type = CustomJsonBinaryType.class),
        @StoredProcedureParameter(name = ExcursionEntity.PAGINATION, mode = IN,
            type = Boolean.class)
    },
        resultClasses = {ExcursionEntity.class}),
    @NamedStoredProcedureQuery(
        name = ExcursionEntity.SEARCH_COUNT,
        procedureName = "excursion_search_count",
        parameters = {
            @StoredProcedureParameter(name = ExcursionEntity.PARAMS, mode = IN,
                type = CustomJsonBinaryType.class),
            @StoredProcedureParameter(name = ExcursionEntity.COUNT, mode = INOUT,
                type = Long.class),
        }
    )}
)
@Accessors(chain = true)
public class ExcursionEntity {

    /**
     * Поиск.
     */
    public static final String SEARCH = "ExcursionEntity.search";

    /**
     * Подсчет общего количества.
     */
    public static final String SEARCH_COUNT = "ExcursionEntity.searchCount";

    /**
     * Параметры поиска.
     */
    public static final String PARAMS = "params";

    /**
     * Параметр пагинации.
     */
    public static final String PAGINATION = "pagination";

    /**
     * Кол-во найденных записей.
     */
    public static final String COUNT = "count";

    /**
     * Идентификатор.
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "UUID", strategy = GenerationType.IDENTITY)
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    /**
     * Описание.
     */
    private String description;

    /**
     * Цена за экскурсию.
     */
    private Integer price;

    /**
     * Агитационные материалы.
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "attraction_id", referencedColumnName = "id")
    private AttractionEntity attraction;

    /**
     * Дополнительная информация.
     */
    private String information;

    /**
     * Ссылка на сайт бронирования экскурсии.
     */
    private String link;
}
