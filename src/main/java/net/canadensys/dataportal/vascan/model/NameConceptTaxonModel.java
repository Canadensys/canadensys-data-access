package net.canadensys.dataportal.vascan.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class NameConceptTaxonModel implements NameConceptModelIF{
	
	private int taxonId;
	private String name;
	private String status;
	

	@Override
	public int getTaxonId() {
		return taxonId;
	}
	@Override
	public void setTaxonId(int taxonId) {
		this.taxonId=taxonId;
	}

	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String name) {
		this.name=name;
	}

	@Override
	public String getStatus() {
		return status;
	}
	@Override
	public void setStatus(String status) {
		this.status=status;
	}
	
	@Override
	public String toString(){
		return new ToStringBuilder(this).append("taxonId", taxonId)
		.append("name", name).toString();
	}

}
