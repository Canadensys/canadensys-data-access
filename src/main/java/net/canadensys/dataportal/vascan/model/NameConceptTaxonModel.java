package net.canadensys.dataportal.vascan.model;

import java.util.List;

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
	
	//Used for synonyms with one parent (almost all of them)
	private Integer parentid;
	private String parentnamehtml;
	
	//Used for synonyms with more than one parent
	//we do not always use an array since it's not common so we avoid unnecessary array creation
	private List<Integer> parentidlist;
	private List<String> parentnamehtmllist;
	
	@Override
	public Integer getTaxonId() {
		return taxonId;
	}
	@Override
	public void setTaxonId(Integer taxonId) {
		this.taxonId=taxonId;
	}

	/**
	 * Used to check if a taxon (a synonym) is linked to only one parent.
	 * @return
	 */
	public boolean hasSingleParent(){
		return (parentid != null);
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
	
	public List<Integer> getParentidlist() {
		return parentidlist;
	}
	public void setParentidlist(List<Integer> parentidlist) {
		this.parentidlist = parentidlist;
	}
	
	public List<String> getParentnamehtmllist() {
		return parentnamehtmllist;
	}
	public void setParentnamehtmllist(List<String> parentnamehtmllist) {
		this.parentnamehtmllist = parentnamehtmllist;
	}
	
	@Override
	public String toString(){
		return new ToStringBuilder(this).append("taxonId", taxonId)
		.append("name", name).toString();
	}

}
