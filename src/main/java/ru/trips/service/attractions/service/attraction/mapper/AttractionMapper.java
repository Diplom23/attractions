package ru.trips.service.attractions.service.attraction.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import ru.trips.contracts.transfer.domain.attractions.AttractionTo;
import ru.trips.service.attractions.domain.entity.AttractionEntity;
import ru.trips.service.attractions.service.excursion.ExcursionCreator;
import ru.trips.service.attractions.service.excursion.ExcursionMapper;

/**
 * Маппер достопримечательностей.
 */
@Mapper(
    uses = {
        ExcursionMapper.class
    }
)
public abstract class AttractionMapper {

    @Autowired
    private ExcursionCreator excursionCreator;

    /**
     * Преобразование сущности в ТО.
     *
     * @param entity - сущность
     * @return - ТО
     */
    public abstract AttractionTo convert(AttractionEntity entity);

    /**
     * Преобразование ТО в сущность.
     *
     * @param entity сущность
     * @param to     ТО
     * @return сущность
     */
    @InheritConfiguration(name = "convert")
    @Mapping(target = "excursions", ignore = true)
    public abstract AttractionEntity convert(@MappingTarget AttractionEntity entity, AttractionTo to);

    /**
     * Маппинг для заполнения экскурсии.
     *
     * @param entity - сущность
     * @param to     - то-модель
     */
    @AfterMapping
    protected void afterMappingFromTo(@MappingTarget AttractionEntity entity,
                                      AttractionTo to) {
        excursionCreator.fillExcursionEntity(entity, to.getExcursions());
    }
}
