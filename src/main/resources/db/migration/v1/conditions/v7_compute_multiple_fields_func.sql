DROP FUNCTION IF EXISTS compute_multiple_fields_condition;
CREATE FUNCTION compute_multiple_fields_condition(params json,
                                 filter character varying,
                                 field_in_filter character varying,
                                 concat_fields_in_db character varying)
    RETURNS character varying
    LANGUAGE plpgsql
AS
$$
DECLARE
    stmt      varchar;
    full_name text[];
    count     int;
    length    int;
    operation text;
BEGIN

    stmt := '';

    IF JSON_EXTRACT_PATH_TEXT(params, filter, field_in_filter) IS NOT NULL THEN

        -- собираем переданное для фильтрации строковое значение в массив
        SELECT REGEXP_SPLIT_TO_ARRAY(JSON_EXTRACT_PATH_TEXT(params, filter, field_in_filter, 'singleValue'), '\s+') INTO full_name;

        operation := JSON_EXTRACT_PATH_TEXT(params, filter, field_in_filter, 'type');

        -- вычисляем длину массива
        length := ARRAY_LENGTH(full_name, 1);

        count := 0;
        WHILE (count < length)
            LOOP
                count := count + 1;
                stmt := stmt || advanced_filter_condition('(' || concat_fields_in_db || ')',
                                                          operation,
                                                          full_name[count],
                                                          NULL);
            END LOOP;
    END IF;
    RETURN stmt;
END
$$;