CREATE schema data;
CREATE TABLE data.jobsite (
jobsite_id serial primary key,
region geometry(Polygon,4326),
editor text,
description text,
startdate date,
enddate date
);

CREATE schema metadata;
CREATE TABLE metadata.changeset_jsonb
(
  changeset_id serial primary key,
  region geometry(Polygon,4326),
  type text,
  schemaname text,
  tablename text,
  metadata text,
  oldvalues jsonb,
  newvalues jsonb,
  reviewed boolean DEFAULT false
);

ALTER TABLE data.jobsite REPLICA IDENTITY full;
