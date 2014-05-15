package net.canadensys.dataportal.occurrence.map;

import java.util.List;
import java.util.Map;

import net.canadensys.query.SearchQueryPart;

/**
 * DAO-like interface of a map server that will provide geospatial related data.
 * The datasource for geospatial could be different from the occurrence data so we want to decouple it.
 * @author canadensys
 *
 */
public interface MapServerAccess {
	
	/**
	 * The map query is a string that represents the criteria into a query that
	 * the map server will understand.
	 * @param searchCriteria
	 * @return
	 */
	public String getMapQuery(Map<String,List<SearchQueryPart>> searchCriteria);
	
	/**
	 * Get the count of all records that will be shown on the map for a specific criteria.
	 * @param searchCriteria
	 * @return
	 */
	public int getGeoreferencedRecordCount(Map<String,List<SearchQueryPart>> searchCriteria);
	
	/**
	 * The center of the map based in the searchCriteria.
	 * The center is based on the centroid
	 * @param sqlString
	 * @return long,lat or an empty array if not found, never null.
	 */
	public String[] getMapCenter(String searchCriteria);
	
	/**
	 * If the map server provides a data layer(grid) on the map, this function will be used
	 * to retrieved details on a specific element.
	 * @param id of the element on the map.
	 * @param columnList column name of the details
	 * @return json string
	 */
	public String getMapElementDetails(int id, List<String> columnList);
	
}
