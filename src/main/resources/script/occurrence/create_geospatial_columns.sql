-- Public schema:
--WSG84 latitude/longitude
SELECT AddGeometryColumn('public','occurrence','the_geom', 4326, 'POINT', 2 );
--WebMercator projection
SELECT AddGeometryColumn('public','occurrence','the_geom_webmercator', 3857, 'POINT', 2 );
--WSG84 shifted latitude/longitude (result of ST_Shift_Longitude)
SELECT AddGeometryColumn('public','occurrence','the_shifted_geom', 4326, 'POINT', 2 );

-- Buffer schema:
--WSG84 latitude/longitude
SELECT AddGeometryColumn('buffer','occurrence','the_geom', 4326, 'POINT', 2 );
--WebMercator projection
SELECT AddGeometryColumn('buffer','occurrence','the_geom_webmercator', 3857, 'POINT', 2 );
--WSG84 shifted latitude/longitude (result of ST_Shift_Longitude)
SELECT AddGeometryColumn('buffer','occurrence','the_shifted_geom', 4326, 'POINT', 2 );

--function that will return NULL as fallback if st_transform cannot be performed
CREATE OR REPLACE FUNCTION st_transform_null(geometry, integer)
  RETURNS geometry AS
$BODY$BEGIN
   RETURN st_transform($1, $2);
EXCEPTION WHEN internal_error THEN
   RETURN NULL;
END;$BODY$
  LANGUAGE plpgsql IMMUTABLE STRICT
  COST 100;
ALTER FUNCTION st_transform_null(geometry, integer)
  OWNER TO postgres;