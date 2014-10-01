--This table should be empty but if not please, transfer the content before running drop table
DROP TABLE occurrence_extension;
CREATE TABLE IF NOT EXISTS occurrence_extension
(
	auto_id bigint NOT NULL,
	dwcaid character varying(75),
	sourcefileid character varying(50),
	resource_uuid character varying(50),
	ext_type character varying(25), 
	ext_version character varying(10), 
	ext_data hstore,
	CONSTRAINT occurrence_extension_pkey PRIMARY KEY (auto_id)
);

CREATE SEQUENCE IF NOT EXISTS buffer.occurrence_extension_id_seq;
CREATE TABLE IF NOT EXISTS buffer.occurrence_extension
(
	auto_id bigint DEFAULT nextval('buffer.occurrence_extension_id_seq') NOT NULL,
	dwcaid character varying(75),
	sourcefileid character varying(50),
	resource_uuid character varying(50),
	ext_type character varying(25), 
	ext_version character varying(10), 
	ext_data hstore
);

ALTER TABLE resource_management RENAME key TO resource_uuid;

ALTER TABLE occurrence ADD COLUMN bibliographiccitation TEXT;
ALTER TABLE occurrence ADD COLUMN occurrenceid TEXT;

ALTER TABLE buffer.occurrence ADD COLUMN bibliographiccitation TEXT;
ALTER TABLE buffer.occurrence ADD COLUMN occurrenceid TEXT;