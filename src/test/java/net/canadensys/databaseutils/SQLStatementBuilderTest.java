package net.canadensys.databaseutils;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.canadensys.dataportal.occurrence.model.OccurrenceModel;

import org.junit.Test;

/**
 * Test for SQLStatementBuilder functionalities
 * @author canadensys
 *
 */
public class SQLStatementBuilderTest {
	
	@Test
	public void testGenerateSQLStatement(){
		assertTrue("SELECT \"colA\" FROM toto WHERE \"colW\" = '18'".equals(SQLStatementBuilder.generateSQLStatement("toto","\"colA\"","\"colW\" = '18'")));
	}
	
	@Test
	public void testGenerateSQLInsert(){
		OccurrenceModel occModel = new OccurrenceModel();
		occModel.setAuto_id(1);
		occModel.setCountry("Sweden");
		Map<String,String> computedField = new HashMap<String, String>();
		computedField.put("computed", "COUNT(tocount)");
		//the order of the fields inside the bean are not guaranteed
		assertTrue("INSERT INTO table1 (country,auto_id,computed) VALUES ('Sweden',1,COUNT(tocount));".equals(SQLStatementBuilder.generateSQLInsert(occModel,"table1",computedField))
				|| "INSERT INTO table1 (auto_id,country,computed) VALUES (1,'Sweden',COUNT(tocount));".equals(SQLStatementBuilder.generateSQLInsert(occModel,"table1",computedField)));
	}
	
	@Test
	public void testGenerateMultipleSQLInsert(){
		List<Object> occList = new ArrayList<Object>();
		OccurrenceModel occModel = new OccurrenceModel();
		occModel.setAuto_id(0);
		occModel.setCountry("Sweden");
		occList.add(occModel);
		occModel = new OccurrenceModel();
		occModel.setAuto_id(1);
		occModel.setCountry("Norway");
		occList.add(occModel);
		
		Map<String,String> computedField = new HashMap<String, String>();
		computedField.put("computed", "COUNT(tocount)");

		//the order of the fields inside the bean are not guaranteed
		assertTrue("INSERT INTO table1 (country,auto_id,computed) VALUES ('Sweden',0,COUNT(tocount)),('Norway',1,COUNT(tocount));".equals(SQLStatementBuilder.generateMultipleRowsSQLInsert(occList,"table1",computedField))
				|| "INSERT INTO table1 (auto_id,country,computed) VALUES (0,'Sweden',COUNT(tocount)),(1,'Norway',COUNT(tocount));".equals(SQLStatementBuilder.generateMultipleRowsSQLInsert(occList,"table1",computedField)));
	}
}
