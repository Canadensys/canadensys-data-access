package net.canadensys.databaseutils;

import static org.junit.Assert.assertEquals;

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

}
