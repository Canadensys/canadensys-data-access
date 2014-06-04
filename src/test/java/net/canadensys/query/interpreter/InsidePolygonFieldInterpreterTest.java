package net.canadensys.query.interpreter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.canadensys.query.QueryOperatorEnum;
import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.TestSearchableFieldBuilder;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;


/**
 * Test for InsidePolygonFieldInterpreter
 * @author canadensys
 *
 */
public class InsidePolygonFieldInterpreterTest {
	
	@Test
	public void testGeoCoordinatesFieldInterpreter(){
		SearchQueryPart sqp = new SearchQueryPart();
		sqp.setSearchableField(TestSearchableFieldBuilder.buildInsidePolygonSearchableField());
		sqp.setOp(QueryOperatorEnum.IN);
		sqp.addValue("-25.363882,131.044922");
		sqp.addValue("-26.985412,130.053265");
		sqp.addValue("-26.979865,130.987986");
		sqp.addValue("-25.363882,131.044922");
		
		sqp.addParsedValue("-25.363882,131.044922", "the_geom", Pair.of("-25.363882","131.044922"));
		sqp.addParsedValue("-26.985412,130.053265", "the_geom", Pair.of("-26.985412","130.053265"));
		sqp.addParsedValue("-26.979865,130.987986", "the_geom", Pair.of("-26.979865","130.987986"));
		sqp.addParsedValue("-25.363882,131.044922", "the_geom", Pair.of("-25.363882","131.044922"));
		
		InsidePolygonFieldInterpreter geoCoorInterpreter = new InsidePolygonFieldInterpreter();
		assertTrue(geoCoorInterpreter.canHandleSearchQueryPart(sqp));
		
		assertEquals("ST_Contains(ST_GeomFromText('POLYGON((131.044922 -25.363882,130.053265 -26.985412,130.987986 -26.979865,131.044922 -25.363882))',4326),the_geom)",
				geoCoorInterpreter.toSQL(sqp).toString());
		
		//add hint to tell the polygon cross the IDL
		sqp.addHint(InsidePolygonFieldInterpreter.IS_CROSSING_IDL_HINT, true);
		assertEquals("ST_Contains(ST_Shift_Longitude(ST_GeomFromText('POLYGON((131.044922 -25.363882,130.053265 -26.985412,130.987986 -26.979865,131.044922 -25.363882))',4326)),the_shifted_geom)",
				geoCoorInterpreter.toSQL(sqp).toString());
	}
	
	/**
	 * A single line is not supported.
	 */
	@Test
	public void testSingleLine(){
		SearchQueryPart sqp = new SearchQueryPart();
		sqp.setSearchableField(TestSearchableFieldBuilder.buildInsidePolygonSearchableField());
		sqp.setOp(QueryOperatorEnum.IN);
		sqp.addValue("-25.363882,131.044922");
		sqp.addValue("-26.985412,130.053265");
		sqp.addValue("-25.363882,131.044922");
		
		sqp.addParsedValue("-25.363882,131.044922", "the_geom", Pair.of("-25.363882","131.044922"));
		sqp.addParsedValue("-26.985412,130.053265", "the_geom", Pair.of("-26.985412","130.053265"));
		sqp.addParsedValue("-25.363882,131.044922", "the_geom", Pair.of("-25.363882","131.044922"));
		
		InsidePolygonFieldInterpreter geoCoorInterpreter = new InsidePolygonFieldInterpreter();
		assertFalse(geoCoorInterpreter.canHandleSearchQueryPart(sqp));
	}
}
