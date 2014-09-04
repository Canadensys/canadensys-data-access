package net.canadensys.dataportal.occurrence.dao;

import net.canadensys.dataportal.occurrence.model.OccurrenceExtensionModel;

/**
 * Interface for accessing occurrence extension data.
 * @author canadensys
 *
 */
public interface OccurrenceExtensionDAO {
	
	/**
	 * Load a OccurrenceExtensionModel from an id
	 * @param id
	 * @return OccurrenceExtensionModel or null if nothing is found
	 */
	public OccurrenceExtensionModel load(Long id);
	
	/**
	 * Save a OccurrenceExtensionModel
	 * @param occurrenceExtensionModel
	 * @return success or not
	 */
	public boolean save(OccurrenceExtensionModel occurrenceExtensionModel);

}
