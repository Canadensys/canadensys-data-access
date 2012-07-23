package net.canadensys.databaseutils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * WARNING : no real SQL injection protection here, you should always use PreparedStatement/Hibernate when possible.
 * This class should be used with controlled data only or for testing/debugging purpose.
 * @author canadensys
 *
 */
public class SQLStatementBuilder {
	
	/**
	 * Generates a SQL statement by string concatenation.
	 * All parameters must be formatted : quoted and comma separated for multiple values.
	 * @param table
	 * @param columns
	 * @param criteria
	 * @param limit max number of result
	 * @return
	 */
	public static String generateSQLStatement(String table, String columns, String criteria,int limit){
		return generateSQLStatement(table, columns, criteria)+SQLHelper.LIMIT+limit;
	}
	
	/**
	 * @see #generateSQLStatement(String, String, String, int)
	 * @param table
	 * @param columns
	 * @param criteria
	 * @return
	 */
	public static String generateSQLStatement(String table, String columns, String criteria){
		return SQLHelper.SELECT+columns+SQLHelper.FROM+table+SQLHelper.WHERE+criteria;
	}
	
	/**
	 * Same as generateSQLInsert(bean,table,null)
	 * @param bean
	 * @param table
	 * @return
	 */
	public static String generateSQLInsert(Object bean,String table){
		return generateSQLInsert(bean,table,null);
	}
	
	/**
	 * Generates an SQL INSERT command for multiple rows ex. INSERT INTO t1 VALUES (a1,b1),(a2,b2);
	 * from a list of Java bean.
	 * This notation take less characters than multiple INSERT command since we do not repeat the
	 * headers.
	 * This function will NULL is a bean do no provide a value for a specific column.
	 * @param beanList
	 * @param table
	 * @param additionalColVal
	 * @return
	 */
	//This function could reuse some of the code in generateSQLInsert but we do not want to slow
	//this latter. generateSQLInsert can take the value at the same time as the data.
	@SuppressWarnings("unchecked")
	public static String generateMultipleRowsSQLInsert(List<Object> beanList,String table,Map<String,String> additionalColVal){
		StringBuilder sqlInsert = null;
		Set<String> columnNameSet = new LinkedHashSet<String>();
		List<ArrayList<String>> valueList = new ArrayList<ArrayList<String>>();

		Map<String,Object> beanDescription;
		Object propObj;
		try {
			//check all possible column names
			for(Object currBean : beanList){
				beanDescription = PropertyUtils.describe(currBean);
				for(String property:beanDescription.keySet()){
					propObj =  beanDescription.get(property);
					if(propObj != null && propObj.getClass() != Class.class){
						columnNameSet.add(property);
					}
				}
			}
			
			//loop over all beans
			ArrayList<String> values = null;
			for(Object currBean : beanList){
				values = new ArrayList<String>();
				beanDescription = PropertyUtils.describe(currBean);
				//get the data by column name to ensure the right order
				for(String property:columnNameSet){
					propObj =  beanDescription.get(property);
					if(propObj == null){
						values.add(SQLHelper.NULL);
					}
					else{
						if(propObj.getClass().equals(String.class)){
							values.add(SQLHelper.STRING_QUOTE+SQLHelper.escapeSQLString(propObj.toString())+SQLHelper.STRING_QUOTE);
						}
						else if(propObj.getClass().equals(Integer.class)){
							values.add(propObj.toString());
						}
					}
				}
				valueList.add(values);
			}
			
			//handle additional column
			if(additionalColVal != null & !additionalColVal.isEmpty()){
				for(String key : additionalColVal.keySet()){
					columnNameSet.add(key);
					for(ArrayList<String> row : valueList){
						row.add(additionalColVal.get(key));
					}
				}
			}
			
			//build INSERT command
			sqlInsert = new StringBuilder(SQLHelper.INSERT + table + " (" + StringUtils.join(columnNameSet,",") + ")" + SQLHelper.VALUES);
			for(ArrayList<String> row : valueList){
				sqlInsert.append("(" + StringUtils.join(row,",") + "),");
			}
			sqlInsert.replace(sqlInsert.length()-1, sqlInsert.length(), ";");
			
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sqlInsert.toString();
	}
	
	/**
	 * Generates a SQL INSERT command from a Java bean
	 * Null properties are skipped (ignored)
	 * @param bean
	 * @param table
	 * @param additionalColVal additional column/value map. Usually computed field(s). This parameter can be null.
	 * The value must be formatted.
	 * @return
	 */
	public static String generateSQLInsert(Object bean,String table,Map<String,String> additionalColVal){
		String sqlInsert = null;
		List<String> columnName = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		//StringEscapeUtils a;
		try {
			@SuppressWarnings("unchecked")
			Map<String,Object> beanDescription = PropertyUtils.describe(bean);
			Object propObj;
			
			for(String property:beanDescription.keySet()){
				propObj =  beanDescription.get(property);
				if(propObj != null && propObj.getClass() != Class.class){
					columnName.add(property);
					if(propObj.getClass().equals(String.class)){
						values.add(SQLHelper.STRING_QUOTE+SQLHelper.escapeSQLString(propObj.toString())+SQLHelper.STRING_QUOTE);
					}
					else if(propObj.getClass().equals(Integer.class)){
						values.add(propObj.toString());
					}
				}
			}
			
			if(additionalColVal != null & !additionalColVal.isEmpty()){
				for(String key : additionalColVal.keySet()){
					columnName.add(key);
					values.add(additionalColVal.get(key));
				}
			}
			
			sqlInsert = SQLHelper.INSERT + table + " (" + StringUtils.join(columnName,",") + ")" + SQLHelper.VALUES + "(" + 
					StringUtils.join(values,",") + ");"; 
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sqlInsert;
	}

}
