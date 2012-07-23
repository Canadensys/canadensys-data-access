package net.canadensys.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.canadensys.databaseutils.SQLHelper;
import net.canadensys.query.interpreter.QueryPartInterpreter;
import net.canadensys.query.interpreter.QueryPartInterpreterResolver;

import org.apache.log4j.Logger;

/**
 * Utility class to help DAO-like class to work with criteria as SearchQueryPart.
 * The characteristic of SearchQueryPart as Map<String,List<SearchQueryPart>> is that all different fields 
 * from the searchCriteriaMap key will be separated with an AND and all different values in the list will be separated with an OR.
 * @author canadensys
 *
 */
public class SearchQueryPartUtils {
	
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(SearchQueryPartUtils.class);
	
	public static String searchCriteriaToSQLWhereClause(Map<String,List<SearchQueryPart>> searchCriteria){
		Iterator<String> fieldIt = searchCriteria.keySet().iterator();
		String fieldName;
		List<SearchQueryPart> valueList;
		List<String> orSeparatedList = new ArrayList<String>();
		List<String> andSeparatedList = new ArrayList<String>();
		
		while(fieldIt.hasNext()){
			orSeparatedList.clear();
			fieldName = fieldIt.next();
			valueList = searchCriteria.get(fieldName);
			
			if(!valueList.isEmpty()){
				for(SearchQueryPart currQueryPart : valueList){
					orSeparatedList.add(convertIntoSQL(currQueryPart));
				}
				
				if(valueList.size() > 1){
					andSeparatedList.add(SQLHelper.or(orSeparatedList));
				}
				else{
					andSeparatedList.add(orSeparatedList.get(0));
				}
			}
		}
		return SQLHelper.and(andSeparatedList);
	}
	
	/**
	 * Converts a SearchQueryPart into a SQL string
	 * @param queryPart  a valid SearchQueryPart object
	 * @return SQL string representing this SearchQueryPart or null if no SQL can be created
	 */
	private static String convertIntoSQL(SearchQueryPart queryPart){
		QueryPartInterpreter intepreter = QueryPartInterpreterResolver.getQueryPartInterpreter(queryPart);
		if(intepreter == null){
			LOGGER.fatal("No interpreter found for " + queryPart);
			return null;
		}
		return intepreter.toSQL(queryPart);
	}
}
