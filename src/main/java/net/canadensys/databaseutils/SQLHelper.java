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
	static final String LIKE = " LIKE ";
	static final String NOT_LIKE = " NOT LIKE ";
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
		return column + "=" + quote(value.toString());
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
		return column + "<>" + quote(value.toString());
	}
	
	/**
	 * LIKE query
	 * @param column
	 * @param value
	 * @return
	 */
	public static String like(String column, String value){
		return column + LIKE + quote(value);
	}
	
	public static String notLike(String column, String value){
		return column + NOT_LIKE + quote(value);
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
	
	/**
	 * If left or right is empty the same provided String is returned unchanged.
	 * @param left
	 * @param right
	 * @return
	 */
	public static String and(String left, String right){
		if(StringUtils.isBlank(left)){
			return right;
		}
		if(StringUtils.isBlank(right)){
			return left;
		}
		return left + " AND " + right;
	}

	public static String and(List<String> andSeparatedList){
		return StringUtils.join(andSeparatedList, " AND ");
	}
	
	/**
	 * Returns a: column IN('element1','element2') SQL segment.
	 * @param column
	 * @param orSeparatedList
	 * @return
	 */
	public static String in(String column, List<String> orSeparatedList){
		return column + " IN ("+quoteList(orSeparatedList)+")";
	}
	
	public static String in(String column, String subSelect){
		return column + " IN (" + subSelect + ")";
	}
	
	public static String notIn(String column, List<String> orSeparatedList){
		return column + " NOT IN ("+quoteList(orSeparatedList)+")";
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
	 * Returns a string representing a SELECT.
	 * Normally used to call a function.
	 * @param str
	 * @param alias
	 * @return 
	 */
	public static String select(String str, String alias){
		return SELECT+str+" "+alias;
	}
	
	public static String limit(int limit){
		return LIMIT+limit;
	}
	
	/**
	 * Quote the provided String
	 * e.g. 'value'
	 * @param value
	 * @return
	 */
	public static String quote(String value){
		return "'" + escapeSQLString(value) + "'";
	}
	
	public static String quoteList(List<String> values){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i < values.size(); i++){
			if(i > 0){
				sb.append(",");
			}
			sb.append(quote(values.get(i)));
		}
		return sb.toString();
	}
	
	/**
	 * This function is NOT a SQL injection protection.
	 * Very simple escape function for SQL string.
	 * All single quotes will be doubled
	 * @param command
	 * @return
	 */
	public static String escapeSQLString(String command){
		return command.replaceAll("'", "''");
	}
}
