package net.canadensys.dataportal.vascan.dao;

import java.util.List;

import net.canadensys.dataportal.vascan.model.NameConceptModelIF;
import net.canadensys.query.LimitedResult;

/**
 * Interface for accessing Vascan names data.
 * A name could be a taxon name or a vernaculor name.
 * @author canandensys
 *
 */
public interface NameDAO {

	/**
	 * Search NameModel from a String
	 * @param text
	 * @return
	 */
	public LimitedResult<List<NameConceptModelIF>> search(String text);
	
	public List<NameConceptModelIF> searchTaxon(String text);
	public List<NameConceptModelIF> searchVernacular(String text);
	
	/**
	 * Search NameModel from a String with paging.
	 * @param text
	 * @param pageNumber (starting at 0)
	 * @return
	 */
	public List<NameConceptModelIF> search(String text, int pageNumber);
	
	/**
	 * Set new page size for search with paging capability.
	 * @param pageSize
	 */
	public void setPageSize(int pageSize);
}
