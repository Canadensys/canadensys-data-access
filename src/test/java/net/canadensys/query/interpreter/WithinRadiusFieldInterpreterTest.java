package net.canadensys.query.interpreter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.canadensys.query.QueryOperatorEnum;
import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.TestSearchableFieldBuilder;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

public class WithinRadiusFieldInterpreterTest {
	
	@Test
	public void testGeoCoordinatesFieldInterpreter(){
		SearchQueryPart sqp = new SearchQueryPart();
		sqp.setSearchableField(TestSearchableFieldBuilder.buildWithinRadiusSearchableField());
		sqp.setOp(QueryOperatorEnum.IN);
		sqp.addValue("-25.363882,131.044922");
		sqp.addValue("250");

		
		sqp.addParsedValue("-25.363882,131.044922", "the_geom", Pair.of("-25.363882","131.044922"));
		sqp.addParsedValue("250", "the_geom", 250);
		
		WithinRadiusFieldInterpreter withinRadiusInterpreter = new WithinRadiusFieldInterpreter();
		assertTrue(withinRadiusInterpreter.canHandleSearchQueryPart(sqp));
		
		assertEquals("ST_DWithin(Geography(ST_SetSRID(ST_MakePoint(131.044922,-25.363882),4326)),Geography(the_geom),250)", withinRadiusInterpreter.toSQL(sqp).toString());
	}
	
	
	/**
	 * Test that non-number values are flag as "can't be handled"
	 */
	@Test
	public void testWrongValues(){
		SearchQueryPart sqp = new SearchQueryPart();
		sqp.setSearchableField(TestSearchableFieldBuilder.buildWithinRadiusSearchableField());
		sqp.setOp(QueryOperatorEnum.IN);
		sqp.addValue("a,b");
		sqp.addValue("250");
		
		sqp.addParsedValue("a,b", "the_geom", Pair.of("a","b"));
		sqp.addParsedValue("250", "the_geom", 250);
		
		WithinRadiusFieldInterpreter withinRadiusInterpreter = new WithinRadiusFieldInterpreter();
		assertFalse(withinRadiusInterpreter.canHandleSearchQueryPart(sqp));
	}

}
