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
	public void testGetCentroidSQLQuery(){
		String sql = PostgisUtils.getCentroidSQLQuery("the_geom", "table", "a = '3'");
		assertEquals("SELECT ST_AsText(st_centroid(st_collect(the_geom))) point FROM table WHERE a = '3'",
				sql);
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
		assertEquals("the_geom && ST_Polygon(ST_GeomFromText('LINESTRING(75.15 29.53,77 29,77.6 29.5,75.15 29.53)'),4326)",
				PostgisUtils.getInsidePolygonSQLClause("the_geom", polygon));
	}
	
	@Test
	public void testGetInsideEnvelopeSQLClause(){
		List<Pair<String,String>> envelope = new ArrayList<Pair<String,String>>();
		envelope.add(Pair.of("75.15", "29.53"));
		envelope.add(Pair.of("77", "29"));
		assertEquals("the_geom && ST_MakeEnvelope(29.53,75.15,29,77,4326)",
				PostgisUtils.getInsideEnvelopeSQLClause("the_geom", envelope));
	}

}
