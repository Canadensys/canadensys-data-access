ALTER TABLE occurrence ADD COLUMN hastypestatus boolean;
ALTER TABLE buffer.occurrence ADD COLUMN hastypestatus boolean;

UPDATE occurrence SET hastypestatus = false;
UPDATE occurrence occ SET hasTypeStatus = true
FROM occurrence_raw occ_raw
WHERE typestatus IS NOT NULL AND typestatus <> '' AND occ_raw.auto_id = occ.auto_id;