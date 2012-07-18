package net.canadensys.query;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import org.junit.Test;

public class SearchQueryPartValidatorTest {
	
	@Test
	public void testValidator(){
		SearchQueryPart sqp = new SearchQueryPart();
		assertFalse(SearchQueryPartValidator.validate(sqp));
		
		sqp.setSearchableField(TestSearchableFieldBuilder.buildSingleValueSearchableField(1,"TestSearchableField","TestField"));
		assertFalse(SearchQueryPartValidator.validate(sqp));
		
		sqp.setOp(QueryOperatorEnum.EQ);
		assertFalse(SearchQueryPartValidator.validate(sqp));
		
		sqp.addValue("t1");
		//this test should pass
		assertTrue(SearchQueryPartValidator.validate(sqp));
		
		sqp.addValue("t2");
		assertFalse(SearchQueryPartValidator.validate(sqp));
		
		sqp.setOp(QueryOperatorEnum.IN);
		//this test should pass
		assertTrue(SearchQueryPartValidator.validate(sqp));
	}

}
