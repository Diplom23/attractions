package ru.trips.service.attractions.service.excursion;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.trips.contracts.transfer.domain.attractions.ExcursionTo;
import ru.trips.service.attractions.domain.entity.ExcursionEntity;

/**
 * Маппер экскурсии.
 */
@Mapper
public abstract class ExcursionMapper {

    /**
     * Преобразование сущности в ТО.
     *
     * @param entity - сущность
     * @return - ТО
     */
    public abstract ExcursionTo convert(ExcursionEntity entity);

    /**
     * Преобразование ТО в сущность.
     *
     * @param entity сущность
     * @param to     ТО
     * @return сущность
     */
    @InheritConfiguration(name = "convert")
    public abstract ExcursionEntity convert(@MappingTarget ExcursionEntity entity, ExcursionTo to);
}
