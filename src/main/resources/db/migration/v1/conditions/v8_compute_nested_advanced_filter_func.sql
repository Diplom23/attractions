DROP FUNCTION IF EXISTS compute_nested_advanced_filter_condition;
CREATE FUNCTION compute_nested_advanced_filter_condition(params json,
                                                         json_path text[],
                                                         field_in_db character varying,
                                                         operation text)
    RETURNS character varying
    LANGUAGE plpgsql
AS
$$
DECLARE
    filter_json    json;
    type_operation text;
BEGIN


    filter_json := params #> json_path;

    IF filter_json IS NOT NULL THEN
        IF operation IS NULL THEN
            type_operation := JSON_EXTRACT_PATH_TEXT(filter_json, 'type');
        ELSE
            type_operation := operation;
        END IF;

        -- вычисленная операция тоже может быть NULL
        IF type_operation IS NOT NULL THEN
            RETURN advanced_filter_condition(field_in_db,
                                             type_operation,
                                             JSON_EXTRACT_PATH_TEXT(filter_json, 'singleValue'),
                                             JSON_EXTRACT_PATH_TEXT(filter_json, 'multipleValue'));
        END IF;
        RETURN '';
    ELSE
        RETURN '';
    END IF;
END;
$$;
