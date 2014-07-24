ALTER TABLE occurrence ADD COLUMN hastypestatus boolean;
ALTER TABLE buffer.occurrence ADD COLUMN hastypestatus boolean;
ALTER TABLE occurrence ADD COLUMN typestatus TEXT;
ALTER TABLE buffer.occurrence ADD COLUMN typestatus TEXT;

UPDATE occurrence SET hastypestatus = false;
UPDATE occurrence occ SET hasTypeStatus = true
FROM occurrence_raw occ_raw
WHERE occ_raw.typestatus IS NOT NULL AND occ_raw.typestatus <> '' AND occ_raw.auto_id = occ.auto_id;

UPDATE occurrence occ SET occ.typeStatus = occ_raw.typeStatus
FROM occurrence_raw occ_raw
WHERE occ_raw.auto_id = occ.auto_id;

--WSG84 shifted latitude/longitude (result of ST_Shift_Longitude)
SELECT AddGeometryColumn('public','occurrence','the_shifted_geom', 4326, 'POINT', 2 );
SELECT AddGeometryColumn('buffer','occurrence','the_shifted_geom', 4326, 'POINT', 2 );

UPDATE occurrence SET the_shifted_geom = ST_Shift_Longitude(the_geom) WHERE the_geom IS NOT NULL;
