package net.canadensys.dataportal.occurrence.map.impl;

import java.util.List;
import java.util.Map;

import net.canadensys.databaseutils.PostgisUtils;
import net.canadensys.databaseutils.SQLHelper;
import net.canadensys.databaseutils.SQLStatementBuilder;
import net.canadensys.dataportal.occurrence.map.MapServerAccess;
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
			LOGGER.error("getMapCenter could not get the center point of " + sql);
		}
		return coordinates;
	}
	
	public String getTableUsed() {
		return tableUsed;
	}

	public void setTableUsed(String tableUsed) {
		this.tableUsed = tableUsed;
	}

}
