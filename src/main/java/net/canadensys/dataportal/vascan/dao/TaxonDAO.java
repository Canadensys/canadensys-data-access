package net.canadensys.dataportal.vascan.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.canadensys.dataportal.vascan.dao.query.RegionQueryPart;
import net.canadensys.dataportal.vascan.model.TaxonLookupModel;
import net.canadensys.dataportal.vascan.model.TaxonModel;

/**
 * Interface for accessing taxon concept including TaxonModel and TaxonLookupModel
 * @author canadensys
 *
 */
public interface TaxonDAO {
	
	public static final String SORT_TAXONOMIC = "taxonomically";
	public static final String SORT_ALPHABETIC = "alphabetically";
	
	//denormalized data (DD) keys
	public static final String DD_ID = "id";
	public static final String DD_MDATE = "mdate";
	public static final String DD_STATUS = "status";
	public static final String DD_CONCAT_PARENT_ID = "concatParentId";
	public static final String DD_URL = "url";
	public static final String DD_REFERENCE = "reference";
	public static final String DD_CALNAME_AUTHOR = "calnameauthor";
	public static final String DD_AUTHOR = "author";
	public static final String DD_RANK = "rank";
	public static final String DD_CONCAT_PARENT_CALNAME_AUTHOR = "concatParentCalNameAuthor";
	public static final String DD_HIGHER_CLASSIFICATION = "higherclassification";
	public static final String DD_CLASS = "class";
	public static final String DD_ORDER = "_order";
	public static final String DD_FAMILY = "family";
	public static final String DD_GENUS = "genus";
	public static final String DD_SUBGENUS = "subgenus";
	public static final String DD_SPECIFIC_EPITHET = "specificepithet";
	public static final String DD_INFRASPECIFIC_EPITHET = "infraspecificepithet";
	public static final String DD_CALHABIT = "calhabit";
	public static final String DD_STATUS_ID = "statusid";
	
	/**
	 * Save a TaxonLookupModel. A TaxonLookupModel contains denormalized fields allowing faster access (than joining).
	 * @return success or not
	 */
	public boolean saveTaxonLookup(TaxonLookupModel tlm);
	
	/**
	 * Remove a taxon from the database. The taxon will not be deleted if it is used as a parent in taxonomy or a hybrid parent.
	 * TODO : also check for vernacular
	 * @param taxonId
	 * @return
	 */
	public boolean deleteTaxon(Integer taxonId);
	
	/**
	 * Load a TaxonLookupModel from an identifier. TaxonLookupModel contains denormalized fields allowing faster access (than joining).
	 * @param taxonId
	 * @return
	 */
	public TaxonLookupModel loadTaxonLookup(Integer taxonId);
	
	/**
	 * Get an Iterator on the TaxonLookupModel matching the criteria.
	 * 
	 * @param limitResultsTo 
	 * @param habitus
	 * @param taxonid
	 * @param rqp
	 * @param status
	 * @param rank
	 * @param includeHybrids
	 * @param sort
	 * @return
	 */
	public Iterator<TaxonLookupModel> searchIterator(int limitResultsTo, String habitus, int taxonid, RegionQueryPart rqp, String[] status, String[] rank, boolean includeHybrids, String sort);
		
	/**
	 * Returns an iterator containing the denormalized data as a result of a search.
	 * 
	 * @param limitResultsTo
	 * @param habitus
	 * @param taxonid
	 * @param rqp
	 * @param status
	 * @param rank
	 * @param includeHybrids
	 * @param sort
	 * @return
	 */
	public Iterator<Map<String,Object>> searchIteratorDenormalized(int limitResultsTo, String habitus, Integer taxonid, RegionQueryPart rqp, String[] status, String[] rank, boolean includeHybrids, String sort);
	
	/**
	 * Count the taxon matching the criteria.
	 * 
	 * @param habitus
	 * @param taxonid
	 * @param rqp
	 * @param status
	 * @param rank
	 * @param includeHybrids
	 * @return the count or null if the query can not be completed
	 */
	public Integer countTaxonLookup(String habitus, int taxonid, RegionQueryPart rqp, String[] status, String[] rank, boolean includeHybrids);

	/**
	 * Get a list of accepted taxon from "class" rank to maximumRank
	 * @param maximumRank
	 * @return List of Object array [id:integer,calname:String,rank:String]
	 */
	public List<Object[]> getAcceptedTaxon(int maximumRank);
	
	/**
	 * Load a TaxonModel from an identifier.
	 * @param taxonId
	 * @param deepLoad should we load data from other table(s) inside this query. (for now it only means vernacular names).
	 * @return
	 */
	public TaxonModel loadTaxon(Integer taxonId, boolean deepLoad);
	
	/**
	 * Load a TaxonModel list from a list of id
	 * @param taxonIdList
	 * @return
	 */
	public List<TaxonModel> loadTaxonList(List<Integer> taxonIdList);
	
	/**
	 * Load a TaxonModel list from a taxon calculated name. More than one TaxonModel can share the same calculated name.
	 * @param taxonName
	 * @return
	 */
	public List<TaxonModel> loadTaxonByName(String taxonCalculatedName);
	
	
	/**
	 * Load denormalized taxon data for a collection of ids.
	 * @param taxonIdList
	 * @return list of Object with the following content: "id","mdate","status","parentid","url","reference","calnameauthor","author","rank","parentfsn","higherclassification","class",
			"order","family","genus","subgenus","specificepithet","infraspecificepithet"
	 */
	public List<Map<String,Object>> loadDenormalizedTaxonData(List<Integer> taxonIdList);

}
