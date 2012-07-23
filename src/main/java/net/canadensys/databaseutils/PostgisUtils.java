package net.canadensys.databaseutils;

import org.apache.commons.lang3.StringUtils;

/**
 * Utility function to handle Postgis commands
 * @author canadensys
 *
 */
public class PostgisUtils {
	private static final String WSG84_SRID = "4326";
	private static final String GEOMETRYFROMTEXT_CMD = "GEOMETRYFROMTEXT('POINT(%s %s)',%s)";
	private static final String CENTROID_SQL = "SELECT ST_AsText(st_centroid(st_collect(%s))) point FROM %s";
	private static final String CENTROID_SQL_WHERE = "SELECT ST_AsText(st_centroid(st_collect(%s))) point FROM %s WHERE %s";
	
	/**
	 * Generate a Postgis command to create a geom value based on the longitude and latitude.
	 * This function uses SRID 4326.
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	public static String longLatToGeom(String longitude, String latitude){
		return String.format(GEOMETRYFROMTEXT_CMD, longitude,latitude, WSG84_SRID);
	}
	
	/**
	 * Extract points from string returned by PostGIS.
	 * POINT(-70 47) will return -70 and 47 in the array.
	 * @param pointStr
	 * @return index 0:longitude, index 1: latitude
	 */
	public static String[] extractPoint(String pointStr){
		String[] point = null;
		if(!StringUtils.isBlank(pointStr)){
			pointStr = pointStr.substring(pointStr.indexOf("(")+1, pointStr.lastIndexOf(")"));
			point = pointStr.split(" ");
		}
		return point;
	}
	
	/**
	 * Return the query to get the centroid of the geomColumn.
	 * @param geomColumn
	 * @param table
	 * @param whereClause optional
	 * @return
	 */
	public static String getCentroidSQLQuery(String geomColumn, String table, String whereClause){
		if(StringUtils.isBlank(whereClause)){
			return String.format(CENTROID_SQL, geomColumn,table);
		}
		return String.format(CENTROID_SQL_WHERE, geomColumn, table, whereClause);
	}
}
