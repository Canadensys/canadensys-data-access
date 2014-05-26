package net.canadensys.dataportal.occurrence.map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MapUtilsTest {
	
	@Test
	public void testIsBBoxCrossingIDL(){
		assertFalse(MapUtils.isBBoxCrossingIDL(0, 0));
		assertFalse(MapUtils.isBBoxCrossingIDL(30, -30));
		
		assertTrue(MapUtils.isBBoxCrossingIDL(-179, 178));
	}

}
