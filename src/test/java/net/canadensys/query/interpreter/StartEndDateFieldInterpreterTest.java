package net.canadensys.query.interpreter;

import static org.junit.Assert.assertEquals;
import net.canadensys.query.QueryOperatorEnum;
import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.TestSearchableFieldBuilder;

import org.hibernate.criterion.Criterion;
import org.junit.Test;

/**
 * Test for StartEndDateFieldInterpreter
 * @author canadensys
 *
 */
public class StartEndDateFieldInterpreterTest {
	    
    @Test
    public void testDate(){
    	SearchQueryPart sqp2007 = new SearchQueryPart();
    	sqp2007.setSearchableField(TestSearchableFieldBuilder.buildStartEndDateSearchableField());
		sqp2007.setOp(QueryOperatorEnum.EQ);
		sqp2007.addValue("2007--");
		sqp2007.addParsedValue("2007--", "syear", 2007);
		
		StartEndDateFieldInterpreter seDateInterpreter = new StartEndDateFieldInterpreter();
		Criterion c = seDateInterpreter.toCriterion(sqp2007);
		assertEquals("syear=2007", c.toString());
		
    	SearchQueryPart sqp20070830 = new SearchQueryPart();
    	sqp20070830.setSearchableField(TestSearchableFieldBuilder.buildStartEndDateSearchableField());
    	sqp20070830.setOp(QueryOperatorEnum.EQ);
		sqp20070830.addValue("2007-08-30");
		sqp20070830.addParsedValue("2007-08-30", "syear", 2007);
		sqp20070830.addParsedValue("2007-08-30", "smonth", 8);
		sqp20070830.addParsedValue("2007-08-30", "sday", 30);
		String sql = seDateInterpreter.toSQL(sqp20070830);
		assertEquals("syear=2007 AND smonth=8 AND sday=30", sql);
    }
    
    @Test
    public void testBetweenDates(){
    	SearchQueryPart sqpBetweenMay2001AndJune2003 = new SearchQueryPart();
    	sqpBetweenMay2001AndJune2003.setSearchableField(TestSearchableFieldBuilder.buildStartEndDateSearchableField());
    	sqpBetweenMay2001AndJune2003.setOp(QueryOperatorEnum.BETWEEN);
    	sqpBetweenMay2001AndJune2003.addValue("2001-05-");
    	sqpBetweenMay2001AndJune2003.addValue("2003-06-");
    	
    	sqpBetweenMay2001AndJune2003.addParsedValue("2001-05-", "syear", 2001);
    	sqpBetweenMay2001AndJune2003.addParsedValue("2001-05-", "smonth", 05);
    	sqpBetweenMay2001AndJune2003.addParsedValue("2003-06-", "syear", 2003);
    	sqpBetweenMay2001AndJune2003.addParsedValue("2003-06-", "smonth", 06);
    			
		StartEndDateFieldInterpreter seDateInterpreter = new StartEndDateFieldInterpreter();
		Criterion c = seDateInterpreter.toCriterion(sqpBetweenMay2001AndJune2003);
		assertEquals("syear>=2001 and syear<=2003 and smonth>=5 and smonth<=6", c.toString());
		
		String sql = seDateInterpreter.toSQL(sqpBetweenMay2001AndJune2003);
		assertEquals("syear>=2001 AND syear<=2003 AND smonth>=5 AND smonth<=6", sql);
    }
}
