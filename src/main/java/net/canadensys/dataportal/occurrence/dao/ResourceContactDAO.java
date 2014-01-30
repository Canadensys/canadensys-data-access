package net.canadensys.dataportal.occurrence.dao;

import java.util.List;

import net.canadensys.dataportal.occurrence.model.ResourceContactModel;

/**
 * Interface for accessing resource contact data.
 * @author canadensys
 */
public interface ResourceContactDAO {
	
	/**
	 * Save a ResourceContactModel
	 * @param resourceContactModel
	 * @return success or not
	 */
	public boolean save(ResourceContactModel resourceContactModel);
	
	/**
	 * Load a ResourceContactModel from an id
	 * @param id
	 * @return ResourceContactModel or null if nothing is found
	 */
	public ResourceContactModel load(Integer id);
	
	/**
	 * Load a list of ResourceContactModel for a sourcefileid.
	 * More than one contact could be linked to the same sourcefileid.
	 * @param sourcefileid
	 * @return ResourceContactModel list or an empty list if nothing is found
	 */
	public List<ResourceContactModel> load(String sourcefileid);

}
