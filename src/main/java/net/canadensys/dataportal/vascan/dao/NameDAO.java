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

	public List<NameModel> search(String text);
}
