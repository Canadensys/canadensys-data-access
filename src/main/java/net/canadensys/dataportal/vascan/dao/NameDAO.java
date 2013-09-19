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
	 * Search NameModel from a String.
	 * @param text
	 * @param useAutocompletion should we use AutoCompletion feature to return partially matched names. 
	 * @return
	 */
	public LimitedResult<List<NameConceptModelIF>> search(String text, boolean useAutocompletion);
	
	/**
	 * Search NameModel from a String with paging.
	 * @param text
	 * @param useAutocompletion should we use AutoCompletion feature to return partially matched names. 
	 * @param pageNumber (starting at 0)
	 * @return
	 */
	public LimitedResult<List<NameConceptModelIF>> search(String text, boolean useAutocompletion, int pageNumber);
	
	/**
	 * Search only taxon type.
	 * Autocompletion will be used.
	 * @param text
	 * @return
	 */
	public List<NameConceptModelIF> searchTaxon(String text);
	
	/**
	 * Search only vernacular type.
	 * Autocompletion will be used.
	 * @param text
	 * @return
	 */
	public List<NameConceptModelIF> searchVernacular(String text);
	
	/**
	 * Initialization function.
	 * Set new page size for search with paging capability.
	 * @param pageSize
	 */
	public void setPageSize(int pageSize);
	/**
	 * Returns the current page size.
	 * @return
	 */
	public int getPageSize();
}
