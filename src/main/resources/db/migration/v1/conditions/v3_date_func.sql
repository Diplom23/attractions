DROP FUNCTION IF EXISTS date_function;
CREATE FUNCTION date_function(field_name varchar, field_value varchar, field_operation varchar) returns varchar
    language plpgsql
as
$$
begin
    if field_value is null then
        return '';
    end if;
    return ' and ' || field_name || ' ' || field_operation || ' ''' || field_value || '''';
end;
$$;

