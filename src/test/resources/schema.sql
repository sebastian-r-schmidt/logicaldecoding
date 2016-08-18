CREATE schema metadata;
CREATE TABLE metadata.changeset
(
  changeset_id bigint primary key auto_increment,
  region geometry,
  type varchar(80),
  schemaname varchar(80),
  tablename varchar(80),
  metadata varchar(80),
  transaction_id integer,
  commit_ts timestamp,
  oldvalues varchar(400),
  newvalues varchar(400),
  reviewed boolean default false
);