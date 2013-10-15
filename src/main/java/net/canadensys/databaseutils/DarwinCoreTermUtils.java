package net.canadensys.databaseutils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
	
	/**
	 * Translate back a word into the original word (Darwin Core term word).
	 * If the provided term is not a database reserved word, the term will be returned unchanged.
	 * e.g. _class will return class
	 * @param word
	 * @return translated word or the provided word unchanged
	 */
	public static String untranslate(String word){
		Set<String> keys = RESERVED_WORDS.keySet();
		
		for(String currKey : keys){
			if(RESERVED_WORDS.get(currKey).equalsIgnoreCase(word)){
				return currKey;
			}
		}
		return word;
	}
	
	/**
	 * Translate the provided Darwin Core term into a database-safe word.
	 * If the provided term is not a database reserved word, the term will be returned unchanged.
	 * e.g. class will return _class
	 * @param dwcTerm
	 * @return translated term or the provided term unchanged
	 */
	public static String translate(String dwcTerm){
		if(RESERVED_WORDS.containsKey(dwcTerm)){
			return RESERVED_WORDS.get(dwcTerm);
		}
		return dwcTerm;
	}
}
