package net.canadensys.dataportal.occurrence.dao;

import java.util.List;

import net.canadensys.dataportal.occurrence.model.ResourceModel;

public interface ResourceDAO {
	
	/**
	 * Load all ResourceModel
	 * @return ResourceModel list or null
	 */
	public List<ResourceModel> loadResources();
	
	/**
	 * Load a ResourceModel from a sourcefileid
	 * @param sourcefileid
	 * @return ResourceModel or null if nothing is found
	 */
	public ResourceModel load(String sourcefileid);
	
	/**
	 * Save a ResourceModel
	 * @param ResourceModel
	 * @return success or not
	 */
	public boolean save(ResourceModel resourceModel);

}
