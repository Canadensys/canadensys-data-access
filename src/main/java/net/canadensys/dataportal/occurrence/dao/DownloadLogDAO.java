package net.canadensys.dataportal.occurrence.dao;

import net.canadensys.dataportal.occurrence.model.DownloadLogModel;

/**
 * Interface for accessing download log data.
 * @author canadensys
 */
public interface DownloadLogDAO {
	
	/**
	 * Save a DownloadLogModel
	 * @param downloadLogModel
	 * @return success or not
	 */
	public boolean save(DownloadLogModel downloadLogModel);
	
	/**
	 * Load a DownloadLogModel from an id
	 * @param id
	 * @return DownloadLogModel or null if nothing is found
	 */
	public DownloadLogModel load(Integer id);
}
