ALTER TABLE resource_contact RENAME dataset_shortname TO sourcefileid;
ALTER TABLE buffer.resource_contact RENAME dataset_shortname TO sourcefileid;

ALTER TABLE resource_contact RENAME dataset_title TO resource_name;
ALTER TABLE buffer.resource_contact RENAME dataset_title TO resource_name;

ALTER TABLE occurrence ADD COLUMN associatedmediamime text;
ALTER TABLE buffer.occurrence ADD COLUMN associatedmediamime text;