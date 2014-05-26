package net.canadensys.databaseutils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Utility function to handle Postgis commands
 * @author canadensys
 *
 */
public class PostgisUtils {
	private static final String WSG84_SRID = "4326";
	private static final String BBOX_INTERSECT_OPERATOR = " && ";
	
	private static final String GEOMETRYFROMTEXT_CMD = "GEOMETRYFROMTEXT('POINT(%s %s)',%s)";
	private static final String EXTENT_SQL = "ST_extent(%s)";
	private static final String CCENTROID_SQL = "ST_centroid(%s)";
	private static final String CCENTROID_TEXT_SQL = "ST_AsText(ST_centroid(%s))";
	
	private static final String CENTROID_SQL = "SELECT ST_AsText(st_centroid(st_collect(%s))) point FROM %s";
	private static final String CENTROID_SQL_WHERE = "SELECT ST_AsText(st_centroid(st_collect(%s))) point FROM %s WHERE %s";
	
	private static final String MAKE_POLYGON_SQL = "ST_GeomFromText('POLYGON((%s))',%s)";
	private static final String MAKE_ENVELOPE_SQL = "ST_MakeEnvelope(%s,%s)";
	
	private static final String SHIFT_LNG_SQL = "ST_Shift_Longitude(%s)";
	private static final String ST_CONTAINS_SQL = "ST_Contains(%s,%s)";
	
	//ST_DWithin(ST_SetSRID(ST_MakePoint(<lng>, <lat>),<srid>), <geom>, <dist_meters>)";
	private static final String WITHIN_DISTANCE_SQL = "ST_DWithin(Geography(ST_SetSRID(ST_MakePoint(%s,%s),%s)),Geography(%s),%d)";
	
	/**
	 * Generates a Postgis command to create a geom value based on the longitude and latitude.
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
	 * Extract a list of points from a string returned by PostGIS.
	 * The ordering of the points will be preserved.
	 * @param pointsStr
	 * @return
	 */
	public static List<String[]> extractPoints(String pointsStr){
		List<String[]> pointList = null;
		if(!StringUtils.isBlank(pointsStr)){
			pointsStr = pointsStr.substring(pointsStr.indexOf("(")+1, pointsStr.lastIndexOf(")"));
			String[] pointsArray = pointsStr.split(",");
			pointList = new ArrayList<String[]>(pointsArray.length*2);
			for(String currPoint : pointsArray){
				pointList.add(currPoint.split(" "));
			}
		}
		return pointList;
	}
	
	/**
	 * Returns the query to get the centroid of the geomColumn.
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
	
	/**
	 * The SQL fragment to select the centroid of the provided column.
	 * @param geomColumn
	 * @param asText return the bbox as text and not binary
	 * @return
	 */
	public static String getCentroidSQL(String geomColumn, boolean asText){
		if(asText){
			return String.format(CCENTROID_TEXT_SQL, geomColumn); 
		}
		return String.format(CCENTROID_SQL, geomColumn); 
	}
	
	/**
	 * The SQL fragment to select the extent of multiple geometry.
	 * @param geomColumn
	 * @return
	 */
	public static String getExtentSQL(String geomColumn){
		return String.format(EXTENT_SQL, geomColumn); 
	}
	
	/**
	 * Generates a Postgis SQL clause to select within a polygon.
	 * @param geomColumn
	 * @param polygon List of Pair<lat,lng>
	 * @param isShiftedGeometry is the target geometry column a shifted geometry column? In other words, should
	 * we use ST_Shift_Longitude for the comparison.
	 * @return
	 */
	public static String getInsidePolygonSQLClause(String geomColumn, List<Pair<String,String>> polygon, boolean isShiftedGeometry){
		List<String> polygonPoints = new ArrayList<String>(polygon.size());
		for(Pair<String,String> curr : polygon){
			//add right (longitude) before left(latitude) since PostGIS wants X,Y
			polygonPoints.add(curr.getRight() + " " + curr.getLeft());
		}
		if(isShiftedGeometry){
			return String.format(ST_CONTAINS_SQL, String.format(SHIFT_LNG_SQL,String.format(MAKE_POLYGON_SQL, StringUtils.join(polygonPoints,","),WSG84_SRID)),geomColumn);
		}
		
		return String.format(ST_CONTAINS_SQL, String.format(MAKE_POLYGON_SQL, StringUtils.join(polygonPoints,","),WSG84_SRID), geomColumn);
	}
	
	/**
	 * Generates a Postgis SQL clause to select within an envelope.
	 * @param geomColumn
	 * @param polygon List of Pair<lat,lng>
	 * @param isShiftedGeometry is the target geometry column a shifted geometry column? In other words, should
	 * we use ST_Shift_Longitude for the comparison.
	 * @return
	 */
	public static String getInsideEnvelopeSQLClause(String geomColumn, List<Pair<String,String>> envelope, boolean isShiftedGeometry){
		List<String> envelopePoints = new ArrayList<String>(4);
		for(Pair<String,String> curr : envelope){
			//add right (longitude) before left(latitude) since PostGIS wants X,Y
			envelopePoints.add(curr.getRight());
			envelopePoints.add(curr.getLeft());
		}
		if(isShiftedGeometry){
			return geomColumn + BBOX_INTERSECT_OPERATOR + String.format(SHIFT_LNG_SQL,String.format(MAKE_ENVELOPE_SQL, StringUtils.join(envelopePoints,","),WSG84_SRID));
		}
		return geomColumn + BBOX_INTERSECT_OPERATOR + String.format(MAKE_ENVELOPE_SQL, StringUtils.join(envelopePoints,","),WSG84_SRID);
	}
	
	/**
	 * Generates a Postgis SQL clause to select records from a coordinates and a radius in meters.
	 * ST_DWithin is used since it can run on the index. But, the ::geography cast has a certain cost.
	 * @param geomColumn
	 * @param lat
	 * @param lng
	 * @param radiusInMeter
	 * @return
	 */
	public static String getFromWithinRadius(String geomColumn, String lat, String lng, int radiusInMeter){
		return String.format(WITHIN_DISTANCE_SQL,lng,lat,WSG84_SRID,geomColumn,radiusInMeter);
	}
	
	/**
	 * Used to bring a shifted longitude back to -180,180 interval.
	 * @param lng
	 * @return
	 */
	public static String unshiftLongitude(String lng){
		double dLng = NumberUtils.toDouble(lng, 0);
		if(dLng > 180){
			return new Double(dLng-360).toString();
		}
		return lng;
	}
}
