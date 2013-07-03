package net.canadensys.dataportal.vascan.dao;

import java.util.List;
import java.util.Set;

import net.canadensys.dataportal.vascan.model.TaxonLookupModel;

/**
 * Interface for accessing taxonomy data. Taxonomy represents links between taxon (including synonym).
 * @author canadensys
 *
 */
public interface TaxonomyDAO {
	
	/**
	 * Get the all the direct children id for a taxonid. Could also get the entire children id list using the recursive flag.
	 * @param taxonId
	 * @param recursive get all children id, including the children of our children and so on
	 * @return all unique children id found
	 */
	public Set<Integer> getChildrenIdSet(Integer taxonId, boolean recursive);
	
	/**
	 * Get the all the direct accepted children id for a taxonid.
	 * This will not traverse the entire tree.
	 * @param taxonId
	 * @return
	 */
	public List<Integer> getAcceptedChildrenIdList(Integer taxonId);
	
	/**
	 * Get the all the synonym id (taxonid) for a taxonid list.
	 * This will not traverse the entire tree.
	 * @param taxonIdList
	 * @return
	 */
	public List<Integer> getSynonymChildrenIdList(List<Integer> taxonIdList);
	
	/**
	 * Build nested sets structure from a taxonid.
	 * The sets are built from the taxonomy tree using the provided taxonid as root.
	 * @param taxonId
	 */
	public void buildNestedSets(Integer taxonId);
	
	/**
	 * Get all the accepted children id for a taxon.
	 * The nested sets will be used.
	 * @param taxonId
	 * @return
	 */
	public List<Integer> getAcceptedChildrenIdListFromNestedSets(Integer taxonId);
	
	/**
	 * Get all accepted children TaxonLookupModel for a taxonId and a list of ranks.
	 * The nested sets will be used and the list will be ordered by the 'left' value.
	 * @param taxonId 
	 * @param acceptedRanks the list of all accepted ranks(inclusive) for the returned children list
	 */
	public List<TaxonLookupModel> getAcceptedChildrenListFromNestedSets(Integer taxonId, String[] acceptedRanks);

}
