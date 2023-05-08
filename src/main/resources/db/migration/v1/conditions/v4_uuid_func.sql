DROP FUNCTION IF EXISTS uuid_function;
CREATE FUNCTION uuid_function(field_name varchar, field_value varchar, operation varchar) returns varchar
    language plpgsql
as
$$
begin
    if field_value is null then
        return '';
    end if;
    return ' and ' || field_name || ' ' || operation || ' ''' || field_value || '''';
end;
$$;