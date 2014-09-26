ALTER TABLE occurrence_extension ADD COLUMN auto_id bigint NOT NULL;
ALTER TABLE occurrence_extension ADD COLUMN dwcaid character varying(75);
ALTER TABLE occurrence_extension ADD COLUMN sourcefileid character varying(50);
ALTER TABLE occurrence_extension ADD COLUMN resource_uuid character varying(50);

--This table should be empty but if not please, transfer the content of 'id' into 'auto_id' and then delete the 'id' column
ALTER TABLE occurrence_extension DROP COLUMN IF EXISTS id;

ALTER TABLE resource_management RENAME key TO resource_uuid;

ALTER TABLE occurrence ADD COLUMN bibliographiccitation TEXT;
ALTER TABLE occurrence ADD COLUMN occurrenceid TEXT;

ALTER TABLE buffer.occurrence ADD COLUMN bibliographiccitation TEXT;
ALTER TABLE buffer.occurrence ADD COLUMN occurrenceid TEXT;