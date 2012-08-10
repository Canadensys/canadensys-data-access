package net.canadensys.query.interpreter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.canadensys.query.QueryOperatorEnum;
import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.TestSearchableFieldBuilder;

import org.junit.Test;

/**
 * Test for SingleValueFieldInterpreter
 * @author canadensys
 *
 */
public class SingleValueFieldInterpreterTest {
	
	@Test
	public void testMinMaxInterpreterBetween(){

		SearchQueryPart sqp = new SearchQueryPart();
		sqp.setSearchableField(TestSearchableFieldBuilder.buildNumericSingleValueSearchableField(1, "country", "countryid", Integer.class));
		sqp.setOp(QueryOperatorEnum.EQ);
		sqp.addValue("4");
		sqp.addParsedValue("4", "countryid", 4);
		
		SingleValueFieldInterpreter svInterpreter = new SingleValueFieldInterpreter();
		assertTrue(svInterpreter.canHandleSearchQueryPart(sqp));
		assertEquals("countryid=4", svInterpreter.toSQL(sqp));
		assertEquals("countryid=4", svInterpreter.toCriterion(sqp).toString());
		
		sqp = new SearchQueryPart();
		sqp.setSearchableField(TestSearchableFieldBuilder.buildSingleValueSearchableField(2, "country", "country"));
		sqp.setOp(QueryOperatorEnum.EQ);
		sqp.addValue("Japa'n");
		sqp.addParsedValue("Japa'n", "country", "Japa'n");
		assertTrue(svInterpreter.canHandleSearchQueryPart(sqp));
		assertEquals("country='Japa''n'", svInterpreter.toSQL(sqp));
		assertEquals("country=Japa'n", svInterpreter.toCriterion(sqp).toString());
		
		//Test float
		sqp = new SearchQueryPart();
		sqp.setSearchableField(TestSearchableFieldBuilder.buildNumericSingleValueSearchableField(3, "latitude", "lat", Float.class));
		sqp.setOp(QueryOperatorEnum.IN);
		sqp.addValue("4.3");
		sqp.addValue("6.7");
		sqp.addParsedValue("4.3", "lat", 4.3);
		sqp.addParsedValue("6.7", "lat", 6.7);
		assertTrue(svInterpreter.canHandleSearchQueryPart(sqp));
		assertEquals("lat IN (4.3,6.7)", svInterpreter.toSQL(sqp));
		assertEquals("lat in (4.3, 6.7)", svInterpreter.toCriterion(sqp).toString());
		
		//Test boolean
		sqp = new SearchQueryPart();
		sqp.setSearchableField(TestSearchableFieldBuilder.buildBooleanSingleValueSearchableField(6, "hassome", "hassomething"));
		sqp.setOp(QueryOperatorEnum.EQ);
		sqp.addValue("true");
		sqp.addParsedValue("true", "hassomething", Boolean.TRUE);
		assertTrue(svInterpreter.canHandleSearchQueryPart(sqp));
		assertEquals("hassomething='true'", svInterpreter.toSQL(sqp));
		assertEquals("hassomething=true", svInterpreter.toCriterion(sqp).toString());
	}

}
