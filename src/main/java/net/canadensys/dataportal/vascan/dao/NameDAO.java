package net.canadensys.dataportal.vascan.dao;

import java.util.List;

import net.canadensys.dataportal.vascan.model.NameModel;

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
	public List<NameModel> search(String text);
	
	/**
	 * Search NameModel from a String with paging.
	 * @param text
	 * @param pageNumber (starting at 0)
	 * @return
	 */
	public List<NameModel> search(String text, int pageNumber);
	
	/**
	 * Set new page size for search with paging capability.
	 * @param pageSize
	 */
	public void setPageSize(int pageSize);
}
