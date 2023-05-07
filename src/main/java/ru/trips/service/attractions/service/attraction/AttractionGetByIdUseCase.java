package ru.trips.service.attractions.service.attraction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.trips.contracts.transfer.domain.attractions.AttractionTo;
import ru.trips.service.attractions.domain.repository.AttractionRepository;
import ru.trips.service.attractions.service.attraction.mapper.AttractionMapper;

import java.util.UUID;

/**
 * Получение записи по id.
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class AttractionGetByIdUseCase {

    private final AttractionMapper mapper;
    private final AttractionRepository attractionRepository;

    /**
     * Поиск по id.
     *
     * @param id id
     * @return to-модель
     */
    public AttractionTo exec(UUID id) {
        return mapper.convert(attractionRepository.findById(id).orElseThrow(RuntimeException::new));
    }
}
