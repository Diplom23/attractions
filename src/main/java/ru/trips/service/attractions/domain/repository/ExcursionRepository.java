package ru.trips.service.attractions.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.trips.service.attractions.domain.entity.ExcursionEntity;

import java.util.UUID;

/**
 * JPA репозиторий экскурсий.
 */
@Repository
public interface ExcursionRepository extends JpaRepository<ExcursionEntity, UUID> {
}
