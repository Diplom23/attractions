package ru.trips.service.attractions.service.attraction;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.trips.contracts.transfer.domain.attractions.AttractionTo;
import ru.trips.contracts.transfer.domain.result.Result;
import ru.trips.service.attractions.domain.repository.AttractionRepository;
import ru.trips.service.attractions.service.attraction.mapper.AttractionMapper;

import javax.persistence.LockModeType;

/**
 * Обновление записи о достопримечательности.
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class AttractionUpdateUseCase {

    private final AttractionMapper mapper;
    private final AttractionRepository attractionRepository;

    /**
     * Редактирование расценок по to-модели.
     *
     * @param to to-модель
     * @return результат
     */
    @Transactional
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @SneakyThrows
    public Result exec(AttractionTo to) {
        String status;
        var existingEntity = attractionRepository.findById(to.getId());
        if (existingEntity.isPresent()) {
            mapper.convert(existingEntity.get(), to);
            attractionRepository.save(existingEntity.get());
            status = "OK";
        } else {
            log.error(String.format("Attraction with id = %s not found!", to.getId()));
            status = "FAIL";
        }
        return Result.builder()
            .withStatus(status)
            .build();
    }
}
