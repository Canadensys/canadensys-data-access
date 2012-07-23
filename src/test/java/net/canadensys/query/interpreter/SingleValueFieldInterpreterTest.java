package net.canadensys.query.interpreter;

import static org.junit.Assert.assertEquals;
import net.canadensys.query.TestSearchableFieldBuilder;
import net.canadensys.query.QueryOperatorEnum;
import net.canadensys.query.SearchQueryPart;

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
		assertEquals("countryid=4", svInterpreter.toSQL(sqp));
		assertEquals("countryid=4", svInterpreter.toCriterion(sqp).toString());
		
		sqp = new SearchQueryPart();
		sqp.setSearchableField(TestSearchableFieldBuilder.buildSingleValueSearchableField(2, "country", "country"));
		sqp.setOp(QueryOperatorEnum.EQ);
		sqp.addValue("Japa'n");
		sqp.addParsedValue("Japa'n", "country", "Japa'n");
		assertEquals("country='Japa''n'", svInterpreter.toSQL(sqp));
		assertEquals("country=Japa'n", svInterpreter.toCriterion(sqp).toString());
		
		sqp = new SearchQueryPart();
		sqp.setSearchableField(TestSearchableFieldBuilder.buildNumericSingleValueSearchableField(3, "latitude", "lat", Float.class));
		sqp.setOp(QueryOperatorEnum.IN);
		sqp.addValue("4.3");
		sqp.addValue("6.7");
		sqp.addParsedValue("4.3", "lat", 4.3);
		sqp.addParsedValue("6.7", "lat", 6.7);
		
		assertEquals("lat IN (4.3,6.7)", svInterpreter.toSQL(sqp));
		assertEquals("lat in (4.3, 6.7)", svInterpreter.toCriterion(sqp).toString());
	}

}
