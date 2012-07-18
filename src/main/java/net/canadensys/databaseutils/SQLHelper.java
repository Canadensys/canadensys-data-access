package net.canadensys.databaseutils;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Collection of utilities functions to generate textual SQL query part.
 * This class is inspired by Hibernates's Restrictions class.
 * This class has been developed to be used with Postgresql. Other vendor could be added later. 
 * @author canadensys
 *
 */
public class SQLHelper {
	//package protected constant
	static final String SELECT = "SELECT ";
	static final String FROM = " FROM ";
	static final String WHERE = " WHERE ";
	static final String LIMIT = " LIMIT ";
	static final String COUNT = " COUNT";
	static final String INSERT = "INSERT INTO ";
	static final String VALUES = " VALUES ";
	static final String NULL = "NULL";
	static final char STRING_QUOTE = '\'';
	
	//Basic SQL formating
	/**
	 * Creates a BETWEEN SQL fragment for Integer value.
	 * The function takes String to allow cases like (8 BETWEEN cola AND colb) and
	 * (cola BETWEEN 1 AND 8)
	 * @param value a value or a column name
	 * @param low a value or a column name
	 * @param high a value or a column name
	 * @return
	 */
	public static String between(String value, String low, String high){
		return value + " BETWEEN " + low + " AND " + high;
	}
	
	/**
	 * Creates a BETWEEN SQL fragment like 18 BETWEEN collow AND colhigh
	 * @param value
	 * @param colLow
	 * @param colHigh
	 * @return
	 */
	public static String between(Object value, String colLow, String colHigh){
		if(Number.class.isAssignableFrom(value.getClass())){
			return value + " BETWEEN " + colLow + " AND " + colHigh;
		}
		return "'" + escapeSQLString(value.toString()) + "' BETWEEN " + colLow + " AND " + colHigh;
	}
	
	public static String eq(String column, Object value){
		if(Number.class.isAssignableFrom(value.getClass())){
			return column + "=" + value;
		}
		return column + "='" + escapeSQLString(value.toString()) + "'";
	}
	
	/**
	 * Not equals to
	 * @param column
	 * @param value
	 * @return
	 */
	public static String neq(String column, Object value){
		if(Number.class.isAssignableFrom(value.getClass())){
			return column + "<>" + value;
		}
		return column + "<>'" + escapeSQLString(value.toString()) + "'";
	}
	
	/**
	 * Greater than or equals
	 * @param column
	 * @param value must extends Number
	 * @return
	 */
	public static String ge(String column, Object value){
		if(Number.class.isAssignableFrom(value.getClass())){
			return column + ">=" + value;
		}
		return null;
	}
	/**
	 * Less than or equals
	 * @param column
	 * @param value must extends Number
	 * @return
	 */
	public static String le(String column, Object value){
		if(Number.class.isAssignableFrom(value.getClass())){
			return column + "<=" + value;
		}
		return null;
	}
	
	public static String or(String left, String right){
		return "(" + left + " OR " + right + ")";
	}
	public static String or(List<String> orSeparatedList){
		return "("+StringUtils.join(orSeparatedList, " OR ")+")";
	}
	
	public static String and(String left, String right){
		return left + " AND " + right;
	}
	public static String and(List<String> andSeparatedList){
		return StringUtils.join(andSeparatedList, " AND ");
	}
	
	
	/**
	 * Returns a string representing a column count with an alias.
	 * Ex. COUNT(thiscolumn) alias
	 * @param columnName
	 * @param alias
	 * @return SQL string with a space as first character
	 */
	public static String count(String columnName, String alias){
		return COUNT+"("+columnName+") "+alias;
	}
	
	/**
	 * Very simple escape function for SQL string.
	 * All single quotes will be doubled
	 * @param command
	 * @return
	 */
	public static String escapeSQLString(String command){
		return command.replaceAll("'", "''");
	}
}
