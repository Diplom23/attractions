package ru.trips.service.attractions.service.attraction;

import static ru.trips.service.attractions.PageUtil.countTotalPages;
import static ru.trips.service.attractions.PageUtil.createPage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.trips.contracts.transfer.PageData;
import ru.trips.contracts.transfer.domain.attractions.AttractionParams;
import ru.trips.contracts.transfer.domain.attractions.AttractionTo;
import ru.trips.service.attractions.domain.repository.AttractionProcedureRepository;
import ru.trips.service.attractions.service.attraction.mapper.AttractionMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Поиск достопримечательностей.
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class AttractionSearchUseCase {

    private final AttractionProcedureRepository procedureRepository;
    private final AttractionMapper mapper;

    /**
     * Поиск достопримечательностей.
     *
     * @param params параметры для поиска
     * @return список достопримечательностей
     */
    @Transactional(readOnly = true)
    public PageData<AttractionTo> exec(AttractionParams params) {

        long totalElements = procedureRepository.searchCount(params);
        List<AttractionTo> content = procedureRepository.search(params).stream()
            .map(mapper::convert)
            .collect(Collectors.toList());

        return createPage(countTotalPages(totalElements, content),
            totalElements,
            content,
            params.getPagination());
    }
}
