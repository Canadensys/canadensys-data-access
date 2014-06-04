package net.canadensys.query.interpreter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.canadensys.query.QueryOperatorEnum;
import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.TestSearchableFieldBuilder;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

/**
 * Test for InsideEnvelopeFieldInterpreter
 * @author canadensys
 *
 */
public class InsideEnvelopeFieldInterpreterTest {
	
	@Test
	public void testInsideEnvelopeFieldInterpreter(){
		SearchQueryPart sqp = new SearchQueryPart();
		sqp.setSearchableField(TestSearchableFieldBuilder.buildInsidePolygonSearchableField());
		sqp.setOp(QueryOperatorEnum.IN);
		sqp.addValue("-25.363882,131.044922");
		sqp.addValue("-26.979865,130.987986");
		
		sqp.addParsedValue("-25.363882,131.044922", "the_geom", Pair.of("-25.363882","131.044922"));
		sqp.addParsedValue("-26.979865,130.987986", "the_geom", Pair.of("-26.979865","130.987986"));
		
		InsideEnvelopeFieldInterpreter geoCoorInterpreter = new InsideEnvelopeFieldInterpreter();
		assertTrue(geoCoorInterpreter.canHandleSearchQueryPart(sqp));
		
		assertEquals("the_geom && ST_MakeEnvelope(131.044922,-25.363882,130.987986,-26.979865,4326)",
				geoCoorInterpreter.toSQL(sqp).toString());
	}

}
