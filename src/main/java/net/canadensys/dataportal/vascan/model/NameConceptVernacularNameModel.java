package net.canadensys.dataportal.vascan.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Model representing a vernacular name concept which means information so an user can know which
 * vernacular it is, its language and the related taxon.
 * @author canadensys
 *
 */
public class NameConceptVernacularNameModel implements NameConceptModelIF{
	private float score;
	private int id;
	private String name;
	private String status;
	private String	language;
	
	//Related taxon data
	private Integer taxonId;
	private String taxonnamehtml;

	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getLang() {
		return language;
	}
	public void setLang(String lang) {
		this.language = lang;
	}
	
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
	
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String getTaxonnamehtml() {
		return taxonnamehtml;
	}
	public void setTaxonnamehtml(String taxonnamehtml) {
		this.taxonnamehtml = taxonnamehtml;
	}
	
	@Override
	public String toString(){
		return new ToStringBuilder(this).append("taxonId", taxonId)
		.append("name", name).toString();
	}
}
