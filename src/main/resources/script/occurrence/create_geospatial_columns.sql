-- Public schema:
--WSG84 latitude/longitude
SELECT AddGeometryColumn('public','occurrence','the_geom', 4326, 'POINT', 2 );
--WebMercator projection
SELECT AddGeometryColumn('public','occurrence','the_geom_webmercator', 3857, 'POINT', 2 );

-- Buffer schema:
--WSG84 latitude/longitude
SELECT AddGeometryColumn('public','occurrence','the_geom', 4326, 'POINT', 2 );
--WebMercator projection
SELECT AddGeometryColumn('public','occurrence','the_geom_webmercator', 3857, 'POINT', 2 );

