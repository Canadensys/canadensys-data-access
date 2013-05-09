package net.canadensys.dataportal.vascan.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * A name could be a taxon name or a vernaculor name.
 * A vernacular name is always linked to a taxon.
 * @author canandensys
 *
 */
public class NameModel {
	
	private int taxonId;
	private String name;
	
	public int getTaxonId() {
		return taxonId;
	}
	public void setTaxonId(int taxonId) {
		this.taxonId = taxonId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString(){
		return new ToStringBuilder(this).append("taxonId", taxonId)
		.append("name", name).toString();
	}

}
