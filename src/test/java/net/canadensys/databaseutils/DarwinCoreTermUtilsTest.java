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
	
	public void testReservedWordseMap(){
		assertEquals("_class", DarwinCoreTermUtils.RESERVED_WORDS.get("class"));
	}
}
