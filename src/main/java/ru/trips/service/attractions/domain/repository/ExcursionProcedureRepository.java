package ru.trips.service.attractions.domain.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.trips.contracts.transfer.domain.attractions.ExcursionParams;
import ru.trips.service.attractions.domain.entity.ExcursionEntity;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

/**
 * Репозиторий вызова процедур и функций.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ExcursionProcedureRepository {

    @PersistenceContext
    private final EntityManager entityManager;
    private final ObjectMapper objectMapper;

    /**
     * Поиск по параметрам.
     *
     * @param params параметры поиска
     * @return список сущностей
     */
    @SneakyThrows
    public List<ExcursionEntity> search(ExcursionParams params) {
        StoredProcedureQuery query =
            entityManager.createNamedStoredProcedureQuery(ExcursionEntity.SEARCH);
        query.setParameter(ExcursionEntity.PARAMS, objectMapper.writeValueAsString(params));
        query.setParameter(ExcursionEntity.PAGINATION, params.getPagination() != null);
        query.setFlushMode(FlushModeType.COMMIT);

        @SuppressWarnings("unchecked")
        List<ExcursionEntity> entities = query.getResultList();

        log.info("Searching by params: found {} excursions ", entities.size());

        return entities;
    }

    /**
     * Подсчет числа экскурсий по параметрам.
     *
     * @param params параметры поиска.
     * @return число экскурсий
     */
    @SneakyThrows
    public long searchCount(ExcursionParams params) {
        StoredProcedureQuery query =
            entityManager.createNamedStoredProcedureQuery(ExcursionEntity.SEARCH_COUNT);

        query.setParameter(ExcursionEntity.PARAMS, objectMapper.writeValueAsString(params));
        query.setFlushMode(FlushModeType.COMMIT);

        long count = (long) query.getOutputParameterValue(ExcursionEntity.COUNT);
        log.info("Searching count by params: found {} excursions", count);
        return count;
    }
}
