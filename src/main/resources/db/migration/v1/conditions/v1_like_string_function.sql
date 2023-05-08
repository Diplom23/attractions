DROP FUNCTION IF EXISTS like_string_func;
CREATE FUNCTION like_string_func(field_name varchar, field_value varchar) RETURNS varchar
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF
        field_value IS NULL THEN
        RETURN '';
    END IF;

    RETURN ' and lower(' || field_name || ') like ' || QUOTE_LITERAL('%' || LOWER(field_value) || '%');
END;
$$;
