package ru.trips.service.attractions.service.attraction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.trips.contracts.transfer.domain.result.Result;
import ru.trips.service.attractions.domain.repository.AttractionRepository;

import java.util.Set;
import java.util.UUID;

/**
 * Удаление записи о достопримечательности.
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class AttractionDeleteUseCase {

    private final AttractionRepository attractionRepository;

    /**
     * Удаление по id.
     *
     * @param ids список id
     * @return статус операции.
     */
    public Result exec(Set<UUID> ids) {
        String status;
        var images = attractionRepository.findAllById(ids);
        if (images.isEmpty()) {
            log.error("Attractions with ids not found, delete failed");
            status = "FAIL";
        } else {
            attractionRepository.deleteAllById(ids);
            status = "OK";
        }
        return Result.builder()
            .withStatus(status)
            .build();
    }
}
