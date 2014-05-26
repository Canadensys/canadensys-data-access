package net.canadensys.dataportal.occurrence.map;

/**
 * Map related utility functions.
 * It is living in the occurrence package since some assumptions are made.
 * @author cgendreau
 *
 */
public class MapUtils {
	
	/**
	 * Check whether or not a bounding box is crossing the International Date Line (IDL).
	 * Assumptions:
	 * -bbox can't do a wraparound
	 * -bounding box coordinates are provided as NE and SW
	 * - 0,0 0,0 will return false
	 * @param neLng East longitude
	 * @param swLng West longitude
	 * @return
	 */
	public static boolean isBBoxCrossingIDL(double neLng, double swLng){
		if(swLng > 0 && neLng < 0){
			return true;
		}
		return false;
	}
	
}
