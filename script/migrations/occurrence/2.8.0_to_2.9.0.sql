
CREATE SEQUENCE resource_management_id_seq;
CREATE TABLE resource_management
(
  id integer DEFAULT nextval('resource_management_id_seq') NOT NULL,
  sourcefileid character varying(50),
  name character varying(255),
  key character varying(36),
  archive_url character varying(255),
  CONSTRAINT resource_management_pkey PRIMARY KEY (id),
  CONSTRAINT resource_management_source_file_id_key UNIQUE (sourcefileid)
);