package net.canadensys.dataportal.occurrence.map.impl;

import java.util.List;
import java.util.Map;

import net.canadensys.databaseutils.PostgisUtils;
import net.canadensys.databaseutils.SQLHelper;
import net.canadensys.databaseutils.SQLStatementBuilder;
import net.canadensys.dataportal.occurrence.map.MapServerAccess;
import net.canadensys.dataportal.occurrence.model.MapInfoModel;
import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.SearchQueryPartUtils;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Windshaft implementation of MapServerAccess. It uses a Postgresql/Postgis database as datasource.
 * @author canadensys
 */
public class WindshaftMapServerAccess implements MapServerAccess {
	//TODO maybe move to configuration
	private static final String GEOSPATIAL_COLUMN = "the_geom";
	private static final String SHIFTED_GEOSPATIAL_COLUMN = "the_shifted_geom";
	private static final String HAS_COORDINATES_CLAUSE = "hascoordinates=true";
	private static final String COUNT_ALL_STATEMENT = SQLHelper.count("*", "count");
	
	//Temporary hack used to cache data on the map server. (Occurrence is case sensitive)
	private static final String DEFAULT_QUERY = "SELECT * FROM Occurrence";
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private String tableUsed;

	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(WindshaftMapServerAccess.class);
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public String getMapQuery(Map<String,List<SearchQueryPart>> searchCriteria){
		if(searchCriteria.isEmpty()){
			return DEFAULT_QUERY;
		}
		return searchCriteriaToSQL("*", searchCriteria);
	}
	
	@Override
	public int getGeoreferencedRecordCount(Map<String, List<SearchQueryPart>> searchCriteria){
		String sqlCmd = searchCriteriaToSQL(COUNT_ALL_STATEMENT, searchCriteria);
		if(searchCriteria.isEmpty()){
			sqlCmd = SQLStatementBuilder.generateSQLStatement(tableUsed,COUNT_ALL_STATEMENT,HAS_COORDINATES_CLAUSE);
		}
		else{
			sqlCmd = SQLHelper.and(sqlCmd, HAS_COORDINATES_CLAUSE);
		}
		SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(sqlCmd);
		sqlQuery.addScalar("count", IntegerType.INSTANCE);
		
		Integer count = (Integer)sqlQuery.uniqueResult();
		if(count == null){
			LOGGER.error("getGeoreferencedOccurrenceCount could not get the boud of " + sqlCmd);
			return 0;
		}
		return count;
	}

	/**
	 * Windshaft is using the same database as the DAO, use the DAO to get the details.
	 * TODO For consistency, we should implement this.
	 */
	@Override
	public String getMapElementDetails(int id,List<String> columnList) {
		throw new UnsupportedOperationException();
	}
	
	private String searchCriteriaToSQL(String columns, Map<String,List<SearchQueryPart>> searchCriteria){
		String whereClause = SearchQueryPartUtils.searchCriteriaToSQLWhereClause(searchCriteria);
		return SQLStatementBuilder.generateSQLStatement(tableUsed, columns, whereClause);
	}
	
	@Override
	public String[] getMapCenter(String searchCriteria){
		String lowerSearchCriteria = searchCriteria.toLowerCase();
		String[] coordinates = null;
		String whereClause = null;
		int whereIdx = lowerSearchCriteria.indexOf(" where");
		if(whereIdx>0){ // 6 = length of " where"
			whereClause = searchCriteria.substring(whereIdx+6, searchCriteria.length());
		}
		
		String sql = PostgisUtils.getCentroidSQLQuery(GEOSPATIAL_COLUMN, tableUsed, whereClause);
		SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(sql);
		sqlQuery.addScalar("point", StringType.INSTANCE);
		
		String point = (String)sqlQuery.uniqueResult();
		if(point != null){
			coordinates = PostgisUtils.extractPoint(point);
		}
		else{
			coordinates = new String[]{"",""};
			LOGGER.debug("getMapCenter could not get the center point of " + sql);
		}
		return coordinates;
	}
	
	@Override
	public MapInfoModel getMapInfo(String searchCriteria){
		String lowerSearchCriteria = searchCriteria.toLowerCase();
		int whereIdx = lowerSearchCriteria.indexOf(" where");
		String whereClause = null;
		if(whereIdx>0){ // 6 = length of " where"
			whereClause = searchCriteria.substring(whereIdx+6, searchCriteria.length());
		}
		
		//select the extent and the centroid of that extent
		String extentSql = PostgisUtils.getExtentSQL(SHIFTED_GEOSPATIAL_COLUMN);
		String sqlColumns = extentSql + " as ext";
		sqlColumns = sqlColumns +","+PostgisUtils.getCentroidSQL(extentSql,true) + " as cent";
		
		String sqlStr = SQLStatementBuilder.generateSQLStatement(tableUsed, sqlColumns, whereClause);
		SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(sqlStr);
		sqlQuery.addScalar("ext", StringType.INSTANCE);
		sqlQuery.addScalar("cent", StringType.INSTANCE);
		
		Object[] results = (Object[])sqlQuery.uniqueResult();
		MapInfoModel mapInfoModel = new MapInfoModel();
		//Initialize with empty data
		mapInfoModel.setCentroid("", "");
		mapInfoModel.setExtent("", "", "", "");
		
		if(results != null && results.length == 2){
			List<String[]> extent = PostgisUtils.extractPoints((String)results[0]);
			if(extent != null && extent.size() == 2){
				String[] extentMin = extent.get(0);
				String[] extentMax = extent.get(1);
				mapInfoModel.setExtent(extentMin[1], PostgisUtils.unshiftLongitude(extentMin[0]), 
						extentMax[1], PostgisUtils.unshiftLongitude(extentMax[0]));
			}
			
			String[] centroid = PostgisUtils.extractPoint((String)results[1]);
			if(centroid !=null && centroid.length == 2){
				//Postgis works in X,Y so invert coordinates to get lat,lng
				mapInfoModel.setCentroid(centroid[1], PostgisUtils.unshiftLongitude(centroid[0]));
			}
		}
		return mapInfoModel;
	}
	
	public String getTableUsed() {
		return tableUsed;
	}

	public void setTableUsed(String tableUsed) {
		this.tableUsed = tableUsed;
	}

}
