package ru.trips.service.attractions.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.trips.contracts.transfer.PageData;
import ru.trips.contracts.transfer.domain.attractions.AttractionParams;
import ru.trips.contracts.transfer.domain.attractions.AttractionTo;
import ru.trips.contracts.transfer.domain.result.Result;
import ru.trips.service.attractions.service.attraction.AttractionCreateUseCase;
import ru.trips.service.attractions.service.attraction.AttractionDeleteUseCase;
import ru.trips.service.attractions.service.attraction.AttractionGetByIdUseCase;
import ru.trips.service.attractions.service.attraction.AttractionSearchUseCase;
import ru.trips.service.attractions.service.attraction.AttractionUpdateUseCase;

import java.util.Set;
import java.util.UUID;

/**
 * Контроллер для взаимодействия с достопримечательностями.
 */
@RestController
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/attraction")
public class AttractionController {

    private final AttractionCreateUseCase createUseCase;
    private final AttractionDeleteUseCase deleteUseCase;
    private final AttractionGetByIdUseCase byIdUseCase;
    private final AttractionUpdateUseCase updateUseCase;
    private final AttractionSearchUseCase searchUseCase;

    /**
     * Создание.
     *
     * @param dto дто
     * @return результат
     */
    @SneakyThrows
    @PostMapping()
    public Result save(@RequestBody AttractionTo dto) {
        return createUseCase.exec(dto);
    }

    /**
     * Обновление.
     *
     * @param dto дто
     * @return результат
     */
    @SneakyThrows
    @PutMapping()
    public Result update(@RequestBody AttractionTo dto) {
        return updateUseCase.exec(dto);
    }

    /**
     * Удаление.
     *
     * @param ids список ключей
     * @return результат
     */
    @SneakyThrows
    @DeleteMapping
    public Result delete(@RequestBody Set<UUID> ids) {
        return deleteUseCase.exec(ids);
    }

    /**
     * Поиск по id.
     *
     * @param id id
     * @return сущность
     */
    @SneakyThrows
    @GetMapping("/{id}")
    public ResponseEntity<AttractionTo> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(byIdUseCase.exec(id));
    }

    /**
     * Поиск.
     *
     * @param params параметры запроса
     * @return данные.
     */
    @SneakyThrows
    @PostMapping("/search")
    public ResponseEntity<PageData<AttractionTo>> searchByParams(
        @RequestBody AttractionParams params) {
        return ResponseEntity.ok(searchUseCase.exec(params));
    }
}
