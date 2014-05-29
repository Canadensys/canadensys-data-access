CREATE SEQUENCE import_log_id_seq;
CREATE TABLE import_log
(
  id integer DEFAULT nextval('import_log_id_seq') NOT NULL,
  sourcefileid character varying(50),
  record_quantity integer,
  updated_by character varying(50),
  import_process_duration_ms integer,
  event_end_date_time timestamp,
  CONSTRAINT import_log_pkey PRIMARY KEY (id )
);

ALTER TABLE occurrence ALTER COLUMN specificepithet TYPE character varying(250);
ALTER TABLE buffer.occurrence ALTER COLUMN specificepithet TYPE character varying(250);
ALTER TABLE occurrence ALTER COLUMN infraspecificepithet TYPE character varying(250);
ALTER TABLE buffer.occurrence ALTER COLUMN infraspecificepithet TYPE character varying(250);