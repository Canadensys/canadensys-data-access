package net.canadensys.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SearchQueryPartTest {
	
	@Test
	public void testSearchQueryPart(){
		//test auto ordering
		SearchQueryPart sqp = new SearchQueryPart();
		sqp.addValue("value0");
		sqp.addValue("value1");
		sqp.addValue("value2");
		assertEquals(sqp.getValueList().toString(), "[value0, value1, value2]");
		
		//test manual ordering
		sqp = new SearchQueryPart();
		sqp.addValue("value2",2);
		sqp.addValue("value1",1);
		sqp.addValue("value0",0);
		assertEquals(sqp.getValueList().toString(), "[value0, value1, value2]");
		
		//test both
		sqp = new SearchQueryPart();
		sqp.addValue("value2");
		sqp.addValue("value1",1);
		sqp.addValue("value0",0);
		assertEquals(sqp.getValueList().toString(), "[value0, value1, value2]");
	}
	
	@Test
	public void testSearchQueryPartAsJson(){
		SearchQueryPart sqp = new SearchQueryPart();
		sqp.setSearchableField(TestSearchableFieldBuilder.buildSingleValueSearchableField(18,"seachablefield1","fieldname"));
		sqp.setOp(QueryOperatorEnum.EQ);
		sqp.addValue("value0");
		sqp.addValue("value1");
		sqp.addValue("value2");
		ObjectMapper om = new ObjectMapper();
		
		try {
			String jsonStr = om.writeValueAsString(sqp);
			System.out.println(jsonStr);
			assertTrue(jsonStr.contains("\"searchableFieldId\":18"));
			assertTrue(jsonStr.contains("\"searchableFieldName\":\"seachablefield1\""));
			assertTrue(jsonStr.contains("\"singleField\":\"fieldname\""));
			assertTrue(jsonStr.contains("\"op\":\"EQ\""));
			assertTrue(jsonStr.contains("\"valueList\":[\"value0\",\"value1\",\"value2\"]"));
		} catch (JsonGenerationException e) {
			fail();
		} catch (JsonMappingException e) {
			fail();
		} catch (IOException e) {
			fail();
		}
	}

}
