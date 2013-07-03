package net.canadensys.dataportal.vascan.dao;

import java.util.List;

import net.canadensys.dataportal.vascan.model.VernacularNameModel;

/**
 * Interface for accessing vernacular names
 * @author canadensys
 *
 */
public interface VernacularNameDAO {

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
	public List<Object[]> loadCompleteVernacularNameData(List<Integer> taxonIdList);
}
