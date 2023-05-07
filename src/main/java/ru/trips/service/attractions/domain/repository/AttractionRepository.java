package ru.trips.service.attractions.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.trips.service.attractions.domain.entity.AttractionEntity;

import java.util.UUID;

/**
 * JPA репозиторий достопримечательностей.
 */
@Repository
public interface AttractionRepository extends JpaRepository<AttractionEntity, UUID> {
}
