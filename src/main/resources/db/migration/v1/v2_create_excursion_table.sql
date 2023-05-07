CREATE TABLE IF NOT EXISTS excursion
(
    id uuid NOT NULL
        CONSTRAINT excursion_id_pk
            PRIMARY KEY,
    description varchar,
    price integer,
    attraction_id uuid
        CONSTRAINT fk_attraction_id
            REFERENCES attraction,
    information varchar,
    link varchar
);