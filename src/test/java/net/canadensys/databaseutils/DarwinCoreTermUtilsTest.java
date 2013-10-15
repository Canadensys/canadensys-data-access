package net.canadensys.databaseutils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test for DarwinCoreTermUtils map
 * @author canadensys
 *
 */
public class DarwinCoreTermUtilsTest {
	
	@Test(expected=java.lang.UnsupportedOperationException.class)
	public void testReservedWordsUnmodifiableMap(){
		DarwinCoreTermUtils.RESERVED_WORDS.put("test", "test");
	}
	
	public void testReservedWords(){
		assertEquals("_class", DarwinCoreTermUtils.RESERVED_WORDS.get("class"));
		
		assertEquals("_order", DarwinCoreTermUtils.translate("order"));
		assertEquals("order", DarwinCoreTermUtils.untranslate("_order"));
		
		//make sure non-reserved word are unchanged
		assertEquals("eventDate", DarwinCoreTermUtils.translate("eventDate"));
		assertEquals("eventDate", DarwinCoreTermUtils.untranslate("eventDate"));
	}
}
