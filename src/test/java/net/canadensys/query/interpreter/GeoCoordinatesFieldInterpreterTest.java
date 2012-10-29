package net.canadensys.query.interpreter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.canadensys.query.QueryOperatorEnum;
import net.canadensys.query.SearchQueryPart;
import net.canadensys.query.TestSearchableFieldBuilder;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

/**
 * Test for GeoCoordinatesFieldInterpreter
 * @author canadensys
 *
 */
public class GeoCoordinatesFieldInterpreterTest {
	
	@Test
	public void testGeoCoordinatesFieldInterpreter(){
		SearchQueryPart sqp = new SearchQueryPart();
		sqp.setSearchableField(TestSearchableFieldBuilder.buildGeoCoordinatesSearchableField());
		sqp.setOp(QueryOperatorEnum.IN);
		sqp.addValue("-25.363882,131.044922");
		sqp.addValue("-26.985412,130.053265");
		sqp.addValue("-26.979865,130.987986");
		sqp.addValue("-25.363882,131.044922");
		
		sqp.addParsedValue("-25.363882,131.044922", "the_geom", Pair.of("-25.363882","131.044922"));
		sqp.addParsedValue("-26.985412,130.053265", "the_geom", Pair.of("-26.985412","130.053265"));
		sqp.addParsedValue("-26.979865,130.987986", "the_geom", Pair.of("-26.979865","130.987986"));
		sqp.addParsedValue("-25.363882,131.044922", "the_geom", Pair.of("-25.363882","131.044922"));
		
		GeoCoordinatesFieldInterpreter geoCoorInterpreter = new GeoCoordinatesFieldInterpreter();
		assertTrue(geoCoorInterpreter.canHandleSearchQueryPart(sqp));
		
		assertEquals("the_geom && ST_Polygon(ST_GeomFromText('LINESTRING(-25.363882 131.044922,-26.985412 130.053265,-26.979865 130.987986,-25.363882 131.044922)'),4326)", geoCoorInterpreter.toCriterion(sqp).toString());
		assertEquals("the_geom && ST_Polygon(ST_GeomFromText('LINESTRING(-25.363882 131.044922,-26.985412 130.053265,-26.979865 130.987986,-25.363882 131.044922)'),4326)", geoCoorInterpreter.toSQL(sqp).toString());
	}
}
