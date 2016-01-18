create table allsupportedtypes(
a_serial serial primary key,
a_numeric numeric(4,3),
a_real real,
a_double double precision,
a_char char(5),
a_varchar character varying(24),
a_text text,
a_boolean boolean,
a_json json,
a_jsonb jsonb,
a_date date,
a_timestamp timestamp,
a_interval interval,
a_tsvector tsvector,
a_uuid uuid,
a_postgis_geom geometry(Polygon,31468)
);