CREATE TABLE IF NOT EXISTS attraction
(
    id uuid NOT NULL
        CONSTRAINT attraction_id_pk
            PRIMARY KEY,
    name varchar,
    city varchar,
    information varchar
);