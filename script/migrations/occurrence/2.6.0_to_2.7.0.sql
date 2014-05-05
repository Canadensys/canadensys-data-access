ALTER TABLE occurrence ADD COLUMN hastypestatus boolean;
ALTER TABLE buffer.occurrence ADD COLUMN hastypestatus boolean;
ALTER TABLE occurrence ADD COLUMN typestatus TEXT;
ALTER TABLE buffer.occurrence ADD COLUMN typestatus TEXT;

UPDATE occurrence SET hastypestatus = false;
UPDATE occurrence occ SET hasTypeStatus = true
FROM occurrence_raw occ_raw
WHERE typestatus IS NOT NULL AND typestatus <> '' AND occ_raw.auto_id = occ.auto_id;

UPDATE occurrence occ SET typeStatus = occ_raw.typeStatus
FROM occurrence_raw occ_raw
WHERE occ_raw.auto_id = occ.auto_id;