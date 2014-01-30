ALTER TABLE occurrence ALTER COLUMN dwcaid TYPE character varying(75);
ALTER TABLE occurrence_raw ALTER COLUMN dwcaid TYPE character varying(75);

ALTER TABLE buffer.occurrence ALTER COLUMN dwcaid TYPE character varying(75);
ALTER TABLE buffer.occurrence_raw ALTER COLUMN dwcaid TYPE character varying(75);
