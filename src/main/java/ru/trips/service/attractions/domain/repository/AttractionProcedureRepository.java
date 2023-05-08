package ru.trips.service.attractions.domain.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.trips.contracts.transfer.domain.attractions.AttractionParams;
import ru.trips.service.attractions.domain.entity.AttractionEntity;

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
public class AttractionProcedureRepository {

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
    public List<AttractionEntity> search(AttractionParams params) {
        StoredProcedureQuery query =
            entityManager.createNamedStoredProcedureQuery(AttractionEntity.SEARCH);
        query.setParameter(AttractionEntity.PARAMS, objectMapper.writeValueAsString(params));
        query.setParameter(AttractionEntity.PAGINATION, params.getPagination() != null);
        query.setFlushMode(FlushModeType.COMMIT);

        @SuppressWarnings("unchecked")
        List<AttractionEntity> entities = query.getResultList();

        log.info("Searching by params: found {} attractions ", entities.size());

        return entities;
    }

    /**
     * Подсчет числа достопримечательностей по параметрам.
     *
     * @param params параметры поиска
     * @return число достопримечательностей
     */
    @SneakyThrows
    public long searchCount(AttractionParams params) {
        StoredProcedureQuery query =
            entityManager.createNamedStoredProcedureQuery(AttractionEntity.SEARCH_COUNT);

        query.setParameter(AttractionEntity.PARAMS, objectMapper.writeValueAsString(params));
        query.setFlushMode(FlushModeType.COMMIT);

        long count = (long) query.getOutputParameterValue(AttractionEntity.COUNT);
        log.info("Searching count by params: found {} attractions", count);
        return count;
    }
}
