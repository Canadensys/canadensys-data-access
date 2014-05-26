package net.canadensys.databaseutils;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

/**
 * Test for PostgisUtils functionalities
 * @author canadensys
 *
 */
public class PostgisUtilsTest {
	
	@Test
	public void testExtractPoint(){
		String[] point = PostgisUtils.extractPoint("POINT(-37.209 45.8753976)");
		assertEquals(point[0],"-37.209");
		assertEquals(point[1],"45.8753976");
	}
	
	@Test
	public void testExtractPoints(){
		List<String[]> points = PostgisUtils.extractPoints("BOX(-40.0833333 -44,103.4166667 39)");
		
		assertEquals(points.get(0)[0],"-40.0833333");
		assertEquals(points.get(0)[1],"-44");
		assertEquals(points.get(1)[0],"103.4166667");
		assertEquals(points.get(1)[1],"39");
	}
	
	@Test
	public void testGetCentroidSQLQuery(){
		String sql = PostgisUtils.getCentroidSQLQuery("the_geom", "table", "a = '3'");
		assertEquals("SELECT ST_AsText(st_centroid(st_collect(the_geom))) point FROM table WHERE a = '3'",
				sql);
	}
	
	@Test
	public void testGetCentroidSQL(){
		String sql = PostgisUtils.getCentroidSQL("the_geom",false);
		assertEquals("ST_centroid(the_geom)", sql);
		
		sql = PostgisUtils.getCentroidSQL("the_geom", true);
		assertEquals("ST_AsText(ST_centroid(the_geom))", sql);
	}
	
	@Test
	public void testGetExtentSQL(){
		String sql = PostgisUtils.getExtentSQL("the_geom");
		assertEquals("ST_extent(the_geom)", sql);
	}
	
	@Test
	public void testGetCentroidSQLQueryWithoutWhere(){
		String sql = PostgisUtils.getCentroidSQLQuery("the_geom", "table", null);
		assertEquals("SELECT ST_AsText(st_centroid(st_collect(the_geom))) point FROM table",
				sql);
	}
	
	@Test
	public void testGetInsidePolygonSQLClause(){
		List<Pair<String,String>> polygon = new ArrayList<Pair<String,String>>();
		polygon.add(Pair.of("29.53","75.15"));
		polygon.add(Pair.of("29","77"));
		polygon.add(Pair.of("29.5","77.6"));
		polygon.add(Pair.of("29.53","75.15"));
		assertEquals("ST_Contains(ST_GeomFromText('POLYGON((75.15 29.53,77 29,77.6 29.5,75.15 29.53))',4326),the_geom)",
				PostgisUtils.getInsidePolygonSQLClause("the_geom", polygon, false));
		
		assertEquals("ST_Contains(ST_Shift_Longitude(ST_GeomFromText('POLYGON((75.15 29.53,77 29,77.6 29.5,75.15 29.53))',4326)),the_shifted_geom)",
				PostgisUtils.getInsidePolygonSQLClause("the_shifted_geom", polygon, true));
	}
	
	@Test
	public void testGetInsideEnvelopeSQLClause(){
		List<Pair<String,String>> envelope = new ArrayList<Pair<String,String>>();
		envelope.add(Pair.of("75.15", "29.53"));
		envelope.add(Pair.of("77", "29"));
		assertEquals("the_geom && ST_MakeEnvelope(29.53,75.15,29,77,4326)",
				PostgisUtils.getInsideEnvelopeSQLClause("the_geom", envelope, false));
		assertEquals("the_shifted_geom && ST_Shift_Longitude(ST_MakeEnvelope(29.53,75.15,29,77,4326))",
				PostgisUtils.getInsideEnvelopeSQLClause("the_shifted_geom", envelope, true));
	}
	
	@Test
	public void testGetFromWithinRadius(){
		assertEquals("ST_DWithin(Geography(ST_SetSRID(ST_MakePoint(29.53,75.15),4326)),Geography(the_geom),250)",
				PostgisUtils.getFromWithinRadius("the_geom", "75.15", "29.53", 250));
	}
	
	@Test
	public void testUnshiftLongitude(){
		assertEquals("-170.0",PostgisUtils.unshiftLongitude("190"));
		assertEquals("15",PostgisUtils.unshiftLongitude("15"));
	}
}
