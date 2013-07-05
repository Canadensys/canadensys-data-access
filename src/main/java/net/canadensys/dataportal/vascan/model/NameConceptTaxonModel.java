package net.canadensys.dataportal.vascan.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Model representing a taxon name concept which means information so an user can know which
 * taxon it is.
 * @author canadensys
 *
 */
public class NameConceptTaxonModel implements NameConceptModelIF{
	
	private Integer taxonId;
	private String name;
	private String status;
	private String namehtml;
	private String namehtmlauthor;
	private String rankname;
	
	//Used for synonyms
	private Integer parentid;
	private String parentnamehtml;
	

	@Override
	public Integer getTaxonId() {
		return taxonId;
	}
	@Override
	public void setTaxonId(Integer taxonId) {
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
	
	public String getRankname() {
		return rankname;
	}
	public void setRankname(String rankname) {
		this.rankname = rankname;
	}
	
	public Integer getParentid() {
		return parentid;
	}
	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}
	
	public String getParentnamehtml() {
		return parentnamehtml;
	}
	public void setParentnamehtml(String parentnamehtml) {
		this.parentnamehtml = parentnamehtml;
	}
	
	@Override
	public String toString(){
		return new ToStringBuilder(this).append("taxonId", taxonId)
		.append("name", name).toString();
	}

}
