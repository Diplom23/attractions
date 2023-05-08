DROP FUNCTION IF EXISTS advanced_filter_condition;
CREATE FUNCTION advanced_filter_condition(field_name character varying, type text, single_value text,
                                          multiple_value text)
    RETURNS character varying
    LANGUAGE plpgsql
AS
$$
DECLARE
    in_condition text;
BEGIN

    IF (type = 'IS_NULL') THEN
        RETURN ' and ' || field_name || ' is null';
    ELSEIF (type = 'IS_NOT_NULL') THEN
        RETURN ' and ' || field_name || ' is not null';
    END IF;

    IF single_value IS NOT NULL OR multiple_value IS NOT NULL THEN
        IF (type = 'EQUALS') THEN
            RETURN ' and ' || field_name || '::text = ' || QUOTE_LITERAL(single_value);
        ELSEIF (type = 'NOT_EQUALS') THEN
                RETURN ' and ' || field_name || '::text != ' || QUOTE_LITERAL(single_value);
        ELSEIF (type = 'EQUALS_IGNORE_CASE') THEN
            RETURN ' and lower(' || field_name || '::text) = ' || LOWER(QUOTE_LITERAL(single_value));
        ELSEIF (type = 'CONTAINS') THEN
            RETURN ' and ' || field_name || '::text ilike ' || QUOTE_LITERAL('%' || LOWER(single_value) || '%');
        ELSEIF (type = 'EQUALS_OR_NULL') THEN
            RETURN ' and (lower(' || field_name || '::text) = ' ||
                   LOWER(QUOTE_LITERAL(single_value)) || ' OR ' || field_name ||' IS NULL) ';
        ELSEIF (type = 'GREATER') THEN
            RETURN ' and ' || field_name || ' > ' || '''' || single_value || ''' ';
        ELSEIF (type = 'GREATER_OR_EQUALS') THEN
            RETURN ' and ' || field_name || ' >= ' || '''' || single_value || ''' ';
        ELSEIF (type = 'LOWER') THEN
            RETURN ' and ' || field_name || ' < ' || '''' || single_value || ''' ';
        ELSEIF (type = 'LOWER_OR_EQUALS') THEN
            RETURN ' and ' || field_name || ' <= ' || '''' || single_value || ''' ';
        ELSEIF (type = 'IN') THEN
            IF multiple_value IS NULL OR JSON_ARRAY_LENGTH(multiple_value::json) = 0 THEN
                RETURN '';
            END IF;

            WITH data AS (SELECT multiple_value::json js)
            SELECT '''' || STRING_AGG(elem, ''',''') || ''''
            INTO in_condition
            FROM data,
                 JSON_ARRAY_ELEMENTS_TEXT(js) elem;

            RETURN ' and ' || field_name || ' in (' || in_condition || ')';
        ELSEIF (type = 'NOT_IN') THEN
            IF multiple_value IS NULL OR JSON_ARRAY_LENGTH(multiple_value::json) = 0 THEN
                RETURN '';
            END IF;

            WITH data AS (SELECT multiple_value::json js)
            SELECT '''' || STRING_AGG(elem, ''',''') || ''''
            INTO in_condition
            FROM data,
                 JSON_ARRAY_ELEMENTS_TEXT(js) elem;

            RETURN ' and ' || field_name || ' not in (' || in_condition || ')';
        ELSEIF (type = 'BETWEEN') THEN
            IF multiple_value IS NULL OR JSON_ARRAY_LENGTH(multiple_value::json) != 2 THEN
                RETURN '';
            END IF;

            WITH data AS (SELECT multiple_value::json js)
            SELECT '''' || STRING_AGG(elem, ''' and ''') || ''''
            INTO in_condition
            FROM data,
                 JSON_ARRAY_ELEMENTS_TEXT(js) elem;

            RETURN ' and (' || field_name || ' between ' || in_condition || ')';
        ELSE
            RAISE 'invalid filter type: %', type;
        END IF;
    ELSE
        RAISE 'singleValue and multipleValue are both null';
    END IF;

END;
$$;