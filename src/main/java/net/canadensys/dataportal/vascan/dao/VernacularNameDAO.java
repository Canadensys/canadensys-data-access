package net.canadensys.dataportal.vascan.dao;

import java.util.List;
import java.util.Map;

import net.canadensys.dataportal.vascan.model.VernacularNameModel;

/**
 * Interface for accessing vernacular names
 * 
 * @author cgendreau
 *
 */
public interface VernacularNameDAO {
	
	//denormalized data (DD) keys
	public static final String DD_TAXON_ID = "taxonid";
	public static final String DD_NAME = "name";
	public static final String DD_REFERENCE = "reference";
	public static final String DD_URL = "url";
	public static final String DD_LANGUAGE = "language";
	public static final String DD_STATUS_ID = "statusid";

	/**
	 * Load a VernacularNameModel from an identifier
	 * @param vernacularNameId
	 * @return
	 */
	public VernacularNameModel loadVernacularName(Integer vernacularNameId);
	
	/**
	 * Load a VernacularNameModel list from a name. More than one VernacularNameModel can share the same name.
	 * @param vernacularName
	 * @return
	 */
	public List<VernacularNameModel> loadVernacularNameByName(String vernacularName);
	
	/**
	 * Load denormalized vernacular name data for a collection of ids.
	 * @param taxonIdList
	 * @return list of Object with the following content: "taxonid","name","reference","url","language","statusid"
	 */
	public List<Map<String,Object>> loadDenormalizedVernacularNameData(List<Integer> taxonIdList);
}
