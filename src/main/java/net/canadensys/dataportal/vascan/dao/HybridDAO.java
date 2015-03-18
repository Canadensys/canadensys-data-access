package net.canadensys.dataportal.vascan.dao;

import java.util.List;
import java.util.Map;

/**
 * Interface for accessing hybrids data.
 * 
 * @author canandensys
 *
 */
public interface HybridDAO {
	
	public static final String DD_TAXON_ID = "childid";
	public static final String DD_PARENT_ID = "parentid";
	public static final String DD_PARENT_CALNAME_AUTHOR = "calnameauthor";

	/**
	 * Load denormalized hybrid parents data for a collection of ids.
	 * 
	 * @param taxonIdList
	 * @return
	 */
	public List<Map<String,Object>> loadDenormalizedHybridParentsData(List<Integer> taxonIdList);
}
