package ru.trips.service.attractions.service.attraction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.trips.contracts.transfer.domain.attractions.AttractionTo;
import ru.trips.contracts.transfer.domain.result.Result;
import ru.trips.service.attractions.domain.entity.AttractionEntity;
import ru.trips.service.attractions.domain.repository.AttractionRepository;
import ru.trips.service.attractions.service.attraction.mapper.AttractionMapper;

import javax.persistence.LockModeType;

/**
 * Создание записи о достопримечательности.
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class AttractionCreateUseCase {

    private final AttractionMapper mapper;
    private final AttractionRepository attractionRepository;

    /**
     * Создание записи о достопримечательности.
     *
     * @param to to-модель
     * @return результат
     */
    @Transactional
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    public Result exec(AttractionTo to) {
        String status;
        try {
            AttractionEntity entity = mapper.convert(new AttractionEntity(), to);
            attractionRepository.save(entity);
            status = "OK";
        } catch (Exception e) {
            log.error("Attraction create error");
            status = "FAIL";
        }
        return Result.builder()
            .withStatus(status)
            .build();
    }
}
