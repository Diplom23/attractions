package ru.trips.service.attractions.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

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
@Accessors(chain = true)
public class ExcursionEntity {

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
