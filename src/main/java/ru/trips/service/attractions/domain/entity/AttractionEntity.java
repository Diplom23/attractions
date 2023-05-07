package ru.trips.service.attractions.domain.entity;

import static java.util.stream.Collectors.toList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Сущность "Достопримечательность".
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "attraction")
@Accessors(chain = true)
public class AttractionEntity {

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
     * Наименование достопримечательности.
     */
    private String name;

    /**
     * Наименование города.
     */
    private String city;

    /**
     * Дополнительная информация.
     */
    private String information;

    /**
     * Информация об экскурсиях.
     */
    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExcursionEntity> excursions = new ArrayList<>();

    /**
     * Создать экскурсию.
     *
     * @param excursion экскурсия
     */
    public void addExcursion(ExcursionEntity excursion) {
        List<UUID> ids = this.excursions.stream()
            .map(ExcursionEntity::getId)
            .filter(Objects::nonNull)
            .collect(toList());

        if (!ids.contains(excursion.getId()) || excursion.getId() == null) {
            excursion.setAttraction(this);
            this.excursions.add(excursion);
        }
    }

    /**
     * Удалить экскурсию.
     *
     * @param excursion экскурсия
     */
    public void removeExcursion(ExcursionEntity excursion) {
        List<UUID> ids = this.excursions.stream()
            .map(ExcursionEntity::getId)
            .collect(toList());

        if (ids.contains(excursion.getId())) {
            excursion.setAttraction(null);
            this.excursions.remove(excursion);
        }
    }
}
