package ru.trips.service.attractions.service.excursion;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.trips.contracts.transfer.domain.attractions.ExcursionTo;
import ru.trips.service.attractions.domain.entity.AttractionEntity;
import ru.trips.service.attractions.domain.entity.ExcursionEntity;
import ru.trips.service.attractions.domain.repository.ExcursionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Создание экскурсий.
 */
@Slf4j
@Component
public class ExcursionCreator {

    private final ExcursionRepository excursionRepository;
    private final ExcursionMapper excursionMapper;

    /**
     * Конструктор.
     *
     * @param excursionRepository репозиторий экскурсий
     * @param excursionMapper маппер экскурсий
     */
    public ExcursionCreator(ExcursionRepository excursionRepository,
                            ExcursionMapper excursionMapper) {
        this.excursionRepository = excursionRepository;
        this.excursionMapper = excursionMapper;
    }

    /**
     * Заполнение экскурсии.
     *
     * @param attractionTo достопримечательность
     * @param excursionEntities список экскурсий
     */
    public void fillExcursionEntity(AttractionEntity attractionTo,
                                    List<ExcursionTo> excursionEntities) {
        List<ExcursionEntity> excursionEntityCopy =
            new ArrayList<>(attractionTo.getExcursions());

        excursionEntityCopy.forEach(excursion -> {
            AttractionEntity attraction = excursion.getAttraction();
            attraction.removeExcursion(excursion);
        });
        if (excursionEntities != null) {
            excursionEntities.stream()
                .map(this::createExcursionEntity)
                .forEach(attractionTo::addExcursion);
        }
    }

    private ExcursionEntity createExcursionEntity(ExcursionTo excursionTo) {
        ExcursionEntity excursion = null;
        if (excursionTo.getId() != null) {
            Optional<ExcursionEntity> excursionEntity = excursionRepository
                .findById(excursionTo.getId());

            if (excursionEntity.isPresent()) {
                excursion = excursionEntity.get();
                excursionMapper.convert(excursion, excursionTo);
            }
        }

        if (excursion == null) {
            excursion = new ExcursionEntity();
            excursionMapper.convert(excursion, excursionTo);
        }

        return excursion;
    }
}
