package ru.trips.service.attractions.service.excursion;

import static ru.trips.service.attractions.PageUtil.countTotalPages;
import static ru.trips.service.attractions.PageUtil.createPage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.trips.contracts.transfer.PageData;
import ru.trips.contracts.transfer.domain.attractions.ExcursionParams;
import ru.trips.contracts.transfer.domain.attractions.ExcursionTo;
import ru.trips.service.attractions.domain.repository.ExcursionProcedureRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Поиск экскурсий.
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class ExcursionSearchUseCase {

    private final ExcursionProcedureRepository procedureRepository;
    private final ExcursionMapper mapper;

    /**
     * Поиск экскурсий.
     *
     * @param params параметры для поиска
     * @return список экскурсий
     */
    @Transactional(readOnly = true)
    public PageData<ExcursionTo> exec(ExcursionParams params) {

        long totalElements = procedureRepository.searchCount(params);
        List<ExcursionTo> content = procedureRepository.search(params).stream()
            .map(mapper::convert)
            .collect(Collectors.toList());

        return createPage(countTotalPages(totalElements, content),
            totalElements,
            content,
            params.getPagination());
    }
}
