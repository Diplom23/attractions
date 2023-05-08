package ru.trips.service.attractions.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.trips.contracts.transfer.PageData;
import ru.trips.contracts.transfer.domain.attractions.AttractionParams;
import ru.trips.contracts.transfer.domain.attractions.AttractionTo;
import ru.trips.contracts.transfer.domain.attractions.ExcursionParams;
import ru.trips.contracts.transfer.domain.attractions.ExcursionTo;
import ru.trips.service.attractions.service.excursion.ExcursionSearchUseCase;

/**
 * Контроллер для взаимодействия с экскурсиями.
 */
@RestController
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/excursion")
public class ExcursionController {

    private final ExcursionSearchUseCase searchUseCase;

    /**
     * Поиск.
     *
     * @param params параметры запроса
     * @return данные.
     */
    @SneakyThrows
    @PostMapping("/search")
    public ResponseEntity<PageData<ExcursionTo>> searchByParams(
        @RequestBody ExcursionParams params) {
        return ResponseEntity.ok(searchUseCase.exec(params));
    }
}
