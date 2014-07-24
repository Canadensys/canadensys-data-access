package net.canadensys.dataportal.occurrence.dao;

import java.util.List;

import net.canadensys.dataportal.occurrence.model.ResourceModel;

public interface ResourceDAO {
	
	/**
	 * Load all ResourceModel
	 * @return ImportLogModel or null if nothing is found
	 */
	public List<ResourceModel> loadResources();
	
	/**
	 * Load a ImportLogModel from an id
	 * @param id
	 * @return ImportLogModel or null if nothing is found
	 */
	public ResourceModel load(String sourcefileid);
	
	/**
	 * Save a ResourceModel
	 * @param ResourceModel
	 * @return success or not
	 */
	public boolean save(ResourceModel resourceModel);

}
