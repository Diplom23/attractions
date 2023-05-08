--liquibase formatted sql
--changeSet runOnChange:true splitStatements:false

DROP FUNCTION IF EXISTS excursion_search_prepare_query;
CREATE FUNCTION excursion_search_prepare_query(countable bool, params json, pagination bool)
    RETURNS text
    LANGUAGE plpgsql
AS
$$
DECLARE
    filter       varchar;
    stmt         varchar;
    page_num     integer;
    page_size    integer;
    sort_stmt    varchar;
    join_sorting text;
BEGIN

    filter := 'filter';

    IF countable THEN
        stmt := 'select count(*) from excursion excursion ';
    ELSE
        stmt := 'select
                  excursion.id,
                  excursion.description,
                  excursion.price,
                  excursion.attraction_id,
                  excursion.information,
                  excursion.link
            from excursion excursion';

        IF JSON_ARRAY_LENGTH(JSON_EXTRACT_PATH(params, 'sorting')) > 0 THEN
            join_sorting := JSON_EXTRACT_PATH_TEXT(params, 'sorting');
        END IF;
    END IF;

    stmt := stmt || ' where 1 = 1 ';

    IF (SELECT REGEXP_MATCHES(join_sorting, 'attractionId'::text)) IS NOT NULL
        OR (JSON_EXTRACT_PATH_TEXT(params, filter, 'attractionName') IS NOT NULL) THEN
        stmt := stmt || ' left outer join attraction attraction
                        on excursion.attraction_id = attraction.id ';
    END IF;

    -- Описание.
    stmt := stmt || compute_advanced_filter_condition(params, filter,
                                                      'description', 'excursion.description', NULL);
    -- Цена.
    stmt := stmt || compute_advanced_filter_condition(params, filter,
                                                      'price', 'excursion.price', NULL);
    -- Достопримечательность.
    stmt := stmt || compute_advanced_filter_condition(params, filter,
                                                      'attractionName', 'attraction.name', NULL);

    -- сортировка
    IF NOT countable THEN
        IF JSON_ARRAY_LENGTH(JSON_EXTRACT_PATH(params, 'sorting')) > 0 THEN

            WITH data AS (SELECT JSON_EXTRACT_PATH(params, 'sorting') AS js)
            SELECT STRING_AGG(LOWER(REGEXP_REPLACE(JSON_EXTRACT_PATH_TEXT(elem, 'parameter'), '([A-Z])', '_\1',
                                                   'g'))
                                  || ' ' || JSON_EXTRACT_PATH_TEXT(elem, 'sortType'), ', ')
            INTO sort_stmt
            FROM data,
                 JSON_ARRAY_ELEMENTS(js) elem;

            stmt := stmt || ' order by ' || sort_stmt;
        END IF;

        IF pagination THEN
            page_num := JSON_EXTRACT_PATH_TEXT(params, 'pagination', 'pageNo')::int;
            page_size := JSON_EXTRACT_PATH_TEXT(params, 'pagination', 'pageSize')::int;
            stmt := stmt || ' limit ' || page_size || ' offset ' || (page_size * page_num);
        END IF;

    END IF;

    RETURN stmt;
END
$$;

DROP FUNCTION IF EXISTS excursion_search_count;
CREATE FUNCTION excursion_search_count(params json, OUT count bigint) RETURNS bigint
    LANGUAGE plpgsql
AS
$$
BEGIN
    EXECUTE excursion_search_prepare_query(TRUE, params, TRUE) INTO count;
END;
$$;

DROP FUNCTION IF EXISTS excursion_search;
CREATE FUNCTION excursion_search(params json, pagination boolean)
    RETURNS TABLE
            (
                id uuid,
                description varchar,
                price integer,
                attraction_id uuid,
                information varchar,
                link varchar
            )
    LANGUAGE plpgsql
AS
$$
BEGIN
    RETURN QUERY EXECUTE excursion_search_prepare_query(FALSE, params, pagination);
END;
$$;