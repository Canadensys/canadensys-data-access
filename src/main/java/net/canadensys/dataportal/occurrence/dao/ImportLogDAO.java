package net.canadensys.dataportal.occurrence.dao;

import net.canadensys.dataportal.occurrence.model.ImportLogModel;

/**
 * Interface for accessing import log data.
 * @author canadensys
 */
public interface ImportLogDAO {
	
	/**
	 * Save a ImportLogModel
	 * @param importLogModel
	 * @return success or not
	 */
	public boolean save(ImportLogModel importLogModel);
	
	/**
	 * Load a ImportLogModel from an id
	 * @param id
	 * @return ImportLogModel or null if nothing is found
	 */
	public ImportLogModel load(Integer id);
}
