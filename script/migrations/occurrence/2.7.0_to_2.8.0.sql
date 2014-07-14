
ALTER TABLE occurrence ADD COLUMN associatedsequences TEXT;
ALTER TABLE occurrence ADD COLUMN basisofrecord VARCHAR(25);
ALTER TABLE buffer.occurrence ADD COLUMN associatedsequences TEXT;
ALTER TABLE buffer.occurrence ADD COLUMN basisofrecord VARCHAR(25);

ALTER TABLE occurrence DROP COLUMN IF EXISTS associatedmediamime;
ALTER TABLE buffer.occurrence DROP COLUMN IF EXISTS associatedmediamime;