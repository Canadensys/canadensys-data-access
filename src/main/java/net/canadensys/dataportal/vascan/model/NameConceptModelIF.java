package net.canadensys.dataportal.vascan.model;


/**
 * A name could be a taxon name or a vernaculor name.
 * A vernacular name is always linked to a taxon.
 * @author canandensys
 *
 */
public interface NameConceptModelIF {
	public void setScore(float score);
	public float getScore();
	
	public Integer getTaxonId();
	public void setTaxonId(Integer taxonId);
	
	public String getName();
	public void setName(String name);

	public String getStatus();
	public void setStatus(String status);
}
