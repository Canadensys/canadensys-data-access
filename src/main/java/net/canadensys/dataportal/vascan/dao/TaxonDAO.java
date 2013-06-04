package net.canadensys.dataportal.vascan.dao;

import java.util.Iterator;
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
	 * Get an Iterator on the TaxonLookupModel matching the criteria 
	 * @param returnCountOnly
	 * @param limitResultsTo
	 * @param combination
	 * @param habitus
	 * @param taxonid
	 * @param province
	 * @param status
	 * @param rank
	 * @param hybrids
	 * @param sort
	 * @return
	 */
	public Iterator<TaxonLookupModel> loadTaxonLookup(int limitResultsTo, String combination, String habitus, int taxonid, String[] province, String[] status, String[] rank, boolean includeHybrids, String sort);
	
	public Integer countTaxonLookup(String combination, String habitus, int taxonid, String[] province, String[] status, String[] rank, boolean includeHybrids);

	/**
	 * 
	 * @param maximumRank
	 * @return List of Object array [id:integer,calname:String,rank:String]
	 */
	public List<Object[]> getAcceptedTaxon(int maximumRank);
	
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
