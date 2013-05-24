package net.canadensys.dataportal.vascan.dao;

import java.util.List;

import net.canadensys.dataportal.vascan.model.TaxonLookupModel;
import net.canadensys.dataportal.vascan.model.TaxonModel;

/**
 * Interface for accessing taxon concept including TaxonModel and TaxonLookupModel
 * @author canadensys
 *
 */
public interface TaxonDAO {
	
	/**
	 * Save a TaxonLookupModel. A TaxonLookupModel contains denormalized fields allowing faster access (than joining).
	 * @return success or not
	 */
	public boolean saveTaxonLookup(TaxonLookupModel tlm);
	
	/**
	 * Load a TaxonLookupModel from an identifier. TaxonLookupModel contains denormalized fields allowing faster access (than joining).
	 * @param taxonId
	 * @return
	 */
	public TaxonLookupModel loadTaxonLookup(Integer taxonId);
	
	/**
	 * Load a TaxonModel from an identifier.
	 * @param vernacularNameId
	 * @return
	 */
	public TaxonModel loadTaxon(Integer taxonId);
	
	/**
	 * Load a TaxonModel list from a taxon calculated name. More than one TaxonModel can share the same calculated name.
	 * @param taxonName
	 * @return
	 */
	public List<TaxonModel> loadTaxonByName(String taxonCalculatedName);

}
