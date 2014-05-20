package net.canadensys.dataportal.occurrence.model;


/**
 * Contains information about a map that may (or may not) be the result of a query.
 * @author cgendreau
 *
 */
public class MapInfoModel {
	
	private String[] extentMin;
	private String[] extentMax;
	
	private String[] centroid;
	
	/**
	 * Set centroid based on lat lng values.
	 * @param lat
	 * @param lng
	 */
	public void setCentroid(String lat, String lng){
		centroid = new String[]{lat,lng};
	}
	
	public void setExtent(String minLat, String minLng, String maxLat, String maxLng){
		extentMin = new String[]{minLat,minLng};
		extentMax = new String[]{maxLat,maxLng};
	}
	
	/**
	 * In the form of lat, lng
	 * @return
	 */
	public String[] getCentroid(){
		return centroid;
	}
	
	/**
	 * Get extent minimum coordinate
	 * @return
	 */
	public String[] getExtentMin(){
		return extentMin;
	}
	/**
	 * Get extent maximum coordinate
	 * @return
	 */
	public String[] getExtentMax(){
		return extentMax;
	}

}
