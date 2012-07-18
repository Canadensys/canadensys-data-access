package net.canadensys.databaseutils;

import org.apache.commons.lang3.StringUtils;

/**
 * Utility class related to PostgreSQL database
 * @author canadensy
 *
 */
public class PostgresUtils {
	
	/**
	 * Under certain conditions, PostgreSQL is faster to count the number of group than to count the number of unique value.
	 * Test your query manually before using this function.
	 * This function will NOT return null value
	 * @param column the column to count the number of unique value
	 * @param whereClause optional formatted where clause
	 * @param table
	 * @return
	 */
	public static String getOptimizedCountDistinctQuery(String column, String whereClause, String table, String alias){
		if(StringUtils.isBlank(whereClause)){
			return "SELECT COUNT(*)"+alias+" FROM(SELECT 1 FROM "+table+" WHERE " + column + "IS NOT NULL GROUP BY " + column + ")cc";
		}
		else{
			return "SELECT COUNT(*)"+alias+" FROM(SELECT 1 FROM "+table+" WHERE " + whereClause +
					" AND " +column + " IS NOT NULL GROUP BY " + column + ")cc";
		}
	}
}
