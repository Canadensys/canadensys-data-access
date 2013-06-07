package net.canadensys.dataportal.vascan.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class NameConceptTaxonModel implements NameConceptModelIF{
	
	private int taxonId;
	private String name;
	private String status;
	private String namehtml;
	private String namehtmlauthor;
	

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
	
	public String getNamehtml() {
		return namehtml;
	}
	public void setNamehtml(String namehtml) {
		this.namehtml = namehtml;
	}
	
	public String getNamehtmlauthor() {
		return namehtmlauthor;
	}
	public void setNamehtmlauthor(String namehtmlauthor) {
		this.namehtmlauthor = namehtmlauthor;
	}
	
	@Override
	public String toString(){
		return new ToStringBuilder(this).append("taxonId", taxonId)
		.append("name", name).toString();
	}

}
