package net.canadensys.databaseutils;

import java.util.HashMap;
import java.util.Map;

/**
 * H2 related decoding functions for testing environment.
 * @author cgendreau
 *
 */
public class H2Decode {
	
	/**
	 * Transform a String "key=>value" into a map. Useful to write INSERT statements in an external source for key/value columns.
	 * @param keyVals
	 * @return
	 */
	public static final Map<String,String> toKeyValue(String ... keyVals) { 
		HashMap<String,String> map = new HashMap<String,String>();
		String[] data = null;
		for(String currData : keyVals){
			data = currData.split("=>");
			map.put(data[0],data[1]);
		}
		return map;
	}

}
