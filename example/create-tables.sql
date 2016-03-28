CREATE schema data;
CREATE TABLE data.jobsite (
jobsite_id serial primary key,
region geometry(Polygon,4326),
editor text,
description text,
startdate date,
enddate date
);
ALTER TABLE data.jobsite REPLICA IDENTITY full;

CREATE schema metadata;
CREATE TABLE metadata.changeset
(
  changeset_id serial primary key,
  region geometry(Polygon,4326),
  type text,
  schemaname text,
  tablename text,
  metadata text,
  transaction_id bigint,
  commit_ts timestamp with time zone,
  oldvalues jsonb,
  newvalues jsonb,
  reviewed boolean DEFAULT false
);
