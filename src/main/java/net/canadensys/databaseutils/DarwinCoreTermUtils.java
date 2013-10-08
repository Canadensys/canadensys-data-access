package net.canadensys.databaseutils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Handle translation for DarwinCore terms that are reserved words for most of the database.
 * @author canadensys
 *
 */
public class DarwinCoreTermUtils {
	
	public static final Map<String,String> RESERVED_WORDS;
	static{
		Map<String,String> reservedWords = new HashMap<String, String>();
		reservedWords.put("class", "_class");
		reservedWords.put("group", "_group");
		reservedWords.put("order", "_order");
		reservedWords.put("references", "_references");
		RESERVED_WORDS = Collections.unmodifiableMap(reservedWords);
	}
}
