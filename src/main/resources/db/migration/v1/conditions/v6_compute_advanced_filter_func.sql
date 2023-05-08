DROP FUNCTION IF EXISTS compute_advanced_filter_condition;
CREATE FUNCTION compute_advanced_filter_condition(params json,
                                                  filter character varying,
                                                  field_in_filter character varying,
                                                  field_in_db character varying,
                                                  operation text)
    RETURNS character varying
    LANGUAGE plpgsql
AS
$$
DECLARE
    type_operation text;
BEGIN

    IF JSON_EXTRACT_PATH_TEXT(params, filter, field_in_filter) IS NOT NULL THEN
        IF operation IS NULL THEN
            type_operation := JSON_EXTRACT_PATH_TEXT(params, filter, field_in_filter, 'type');
        ELSE
            type_operation := operation;
        END IF;

        -- вычисленная операция тоже может быть NULL
        IF type_operation IS NOT NULL THEN
            RETURN advanced_filter_condition(field_in_db,
                                             type_operation,
                                             JSON_EXTRACT_PATH_TEXT(params, filter, field_in_filter, 'singleValue'),
                                             JSON_EXTRACT_PATH_TEXT(params, filter, field_in_filter, 'multipleValue'));
        END IF;
        RETURN '';
    ELSE
        RETURN '';
    END IF;
END;
$$;
