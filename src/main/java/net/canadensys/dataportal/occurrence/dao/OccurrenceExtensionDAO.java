package net.canadensys.dataportal.occurrence.dao;

import java.util.List;

import net.canadensys.dataportal.occurrence.model.OccurrenceExtensionModel;

/**
 * Interface for accessing occurrence extension data.
 * The main reason to NOT embed OccurrenceExtensionModel into OccurrenceModel is that we, in most of the case,
 * don't want to load all extension data provided, we want to specify an extension type.
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
	 * Load all OccurrenceExtensionModel of a certain type linked to a resourceUUID/dwcaId
	 * @param extensionType e.g. multimedia
	 * @param resourceUUID
	 * @param dwcaId
	 * @return list of matching OccurrenceExtensionModel or empty list
	 */
	public List<OccurrenceExtensionModel> load(String extensionType, String resourceUUID, String dwcaId);
	
	/**
	 * Save a OccurrenceExtensionModel
	 * @param occurrenceExtensionModel
	 * @return success or not
	 */
	public boolean save(OccurrenceExtensionModel occurrenceExtensionModel);

}
