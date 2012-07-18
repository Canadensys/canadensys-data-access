package net.canadensys.databaseutils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Test for SQLHelper functionalities
 * @author canadensys
 *
 */
public class SQLHelperTest {
	
	/**
	 * Test SQL operators
	 */
	@Test
	public void testSQLOperators(){
		//equals
		assertEquals("col1='val1'",SQLHelper.eq("col1","val1"));
		assertEquals("col1=12",SQLHelper.eq("col1",12));
		
		//not equals
		assertEquals("col1<>'val1'",SQLHelper.neq("col1","val1"));
		assertEquals("col1<>12",SQLHelper.neq("col1",12));
		
		//greater than
		assertEquals("col1>=12",SQLHelper.ge("col1",12));
		
		//less than
		assertEquals("col1<=12",SQLHelper.le("col1",12));
		
		//between
		assertEquals("12 BETWEEN col1 AND col2",SQLHelper.between(12, "col1", "col2"));
		assertEquals("col1 BETWEEN 2 AND 3",SQLHelper.between("col1", "2", "3"));
		
		List<String> statementList = new ArrayList<String>();
		statementList.add("col1=4");
		statementList.add("col2=3");
		statementList.add("col3=2");
		
		//or
		assertEquals("(col1=4 OR col2=3)",SQLHelper.or("col1=4","col2=3"));
		assertEquals("(col1=4 OR col2=3 OR col3=2)",SQLHelper.or(statementList));
		
		//and
		assertEquals("col1=4 AND col2=3",SQLHelper.and("col1=4","col2=3"));
		assertEquals("col1=4 AND col2=3 AND col3=2",SQLHelper.and(statementList));
	}
	
	@Test
	public void testEscapeSQL(){
		assertTrue("j''aime le sql".equals(SQLHelper.escapeSQLString("j'aime le sql")));
		assertTrue("j''''aime le sql".equals(SQLHelper.escapeSQLString("j''aime le sql")));
		assertTrue("j''''''aime le sql".equals(SQLHelper.escapeSQLString("j'''aime le sql")));
	}

}
