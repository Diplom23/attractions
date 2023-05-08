package ru.trips.service.attractions;

import lombok.experimental.UtilityClass;
import ru.trips.contracts.transfer.PageData;
import ru.trips.contracts.transfer.PageMetaData;
import ru.trips.contracts.transfer.Pagination;

import java.util.List;

/**
 * Утилитарный класс для формирования страниц с результатами поиска.
 */
@UtilityClass
public class PageUtil {

    /**
     * Создание страницы с результатами поиска.
     *
     * @param totalPages    общее количество страниц
     * @param totalElements общее количество найденных элементов
     * @param results       список с искомыми данными
     * @param pagination    информация о пейджинге
     * @param <R>           тип результата
     * @return страница с результатами поиска
     */
    public static <R> PageData<R> createPage(int totalPages, Long totalElements, List<R> results,
                                             Pagination pagination) {
        var pageData = new PageData<R>();
        PageMetaData metaData = new PageMetaData();
        pageData.setMetaData(metaData);
        metaData.setNumber(pagination.getPageNo());

        metaData.setTotalPages(totalPages);
        metaData.setTotalElements(totalElements);
        metaData.setNumber(pagination.getPageNo());
        metaData.setSize(pagination.getPageSize());

        pageData.setContent(results);

        return pageData;
    }

    /**
     * Подсчёт общего количества страниц.
     *
     * @param totalElements - общее количество элементов в БД
     * @param content       - набор элементов за один запрос
     * @return общее количество страниц
     */
    public static int countTotalPages(long totalElements, List<?> content) {
        return totalElements == 0 ? 1 : (int) Math.ceil((double) totalElements / (double) content.size());
    }
}