package net.canadensys.query.interpreter;

import static org.junit.Assert.assertEquals;

import net.canadensys.query.TestSearchableFieldBuilder;
import net.canadensys.query.QueryOperatorEnum;
import net.canadensys.query.SearchQueryPart;

import org.junit.Test;

/**
 * Test for MinMaxNumberFieldInterpreter
 * @author canadensys
 *
 */
public class MinMaxNumberFieldInterpreterTest {
	
	@Test
	public void testMinMaxInterpreterBetween(){
		
		//test auto ordering
		SearchQueryPart sqp = new SearchQueryPart();
		sqp.setSearchableField(TestSearchableFieldBuilder.buildMinMaxIntegerSearchableField());
		sqp.setOp(QueryOperatorEnum.BETWEEN);
		sqp.addValue("4");
		sqp.addValue("8");
		
		sqp.addParsedValue("4", "min", 4);
		sqp.addParsedValue("8", "max", 8);
		
		MinMaxNumberFieldInterpreter mmInterpreter = new MinMaxNumberFieldInterpreter();
		assertEquals("min>=4 and min<=8 or max>=4 and max<=8", mmInterpreter.toCriterion(sqp).toString());
		assertEquals("(4 BETWEEN min AND max OR 8 BETWEEN min AND max)", mmInterpreter.toSQL(sqp));
	}
	
	@Test
	public void testMinMaxInterpreterEquals(){
		SearchQueryPart sqp = new SearchQueryPart();
		sqp.setSearchableField(TestSearchableFieldBuilder.buildMinMaxIntegerSearchableField());
		sqp.setOp(QueryOperatorEnum.EQ);
		sqp.addValue("5");
		
		sqp.addParsedValue("5", "min", 5);
		sqp.addParsedValue("5", "max", 5);
		
		MinMaxNumberFieldInterpreter mmInterpreter = new MinMaxNumberFieldInterpreter();
		assertEquals("min<=5 and max>=5", mmInterpreter.toCriterion(sqp).toString());
		assertEquals("5 BETWEEN min AND max", mmInterpreter.toSQL(sqp).toString());
	}
}
