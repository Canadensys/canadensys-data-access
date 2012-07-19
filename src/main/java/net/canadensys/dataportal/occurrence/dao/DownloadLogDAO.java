package net.canadensys.dataportal.occurrence.dao;

import net.canadensys.dataportal.occurrence.model.DownloadLogModel;

/**
 * Interface for accessing download log data.
 * @author canadensys
 */
public interface DownloadLogDAO {
	
	public boolean save(DownloadLogModel downloadLogModel);
	public DownloadLogModel load(Integer id);
}
