package net.canadensys.dataportal.vascan.dao;

import java.util.Set;

/**
 * Interface for accessing taxonomy data. Taxonomy represents links between taxon (including synonym).
 * @author canadensys
 *
 */
public interface TaxonomyDAO {
	
	/**
	 * Get the all the direct children id for a taxon. Could also get the entire children id list using the recursive flag.
	 * @param taxonId
	 * @param recursive get all children id, including the children of our children and so on
	 * @return all unique children id found
	 */
	public Set<Integer> getChildrenIdSet(Integer taxonId, boolean recursive);

}
