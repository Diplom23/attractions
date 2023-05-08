DROP FUNCTION IF EXISTS equals_string_function;
CREATE FUNCTION equals_string_function(field_name varchar, field_value varchar) RETURNS varchar
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF
        field_value IS NULL THEN
        RETURN '';
    END IF;

    RETURN ' and lower(' || field_name || ') = ' || QUOTE_LITERAL(LOWER(field_value));
END;
$$;
