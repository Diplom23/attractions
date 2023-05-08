package ru.trips.service.attractions.service.excursion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.trips.contracts.transfer.domain.attractions.ExcursionTo;
import ru.trips.service.attractions.domain.repository.ExcursionRepository;

import java.util.UUID;

/**
 * Получение записи по id.
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class ExcursionGetByIdUseCase {

    private final ExcursionMapper mapper;
    private final ExcursionRepository attractionRepository;

    /**
     * Поиск по id.
     *
     * @param id id
     * @return to-модель
     */
    public ExcursionTo exec(UUID id) {
        return mapper.convert(attractionRepository.findById(id).orElseThrow(RuntimeException::new));
    }
}
