/*
	Copyright (c) 2010-2013 Canadensys
*/
package net.canadensys.dataportal.vascan.model;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name="taxon")
public class TaxonModel implements Comparable<TaxonModel>{

	private Integer					id;
	private String					uninomial;
	private String					binomial;
	private String					trinomial;
	private String					quadrinomial;
	private String					author;
	private StatusModel				status;
	private RankModel				rank;
	private ReferenceModel			reference;
	private String					commentary;
	private int						notaxon;
	@Temporal(TemporalType.TIMESTAMP)
	private	Date					cdate;
	@Temporal(TemporalType.TIMESTAMP)
	private	Date					mdate;
    private List<HabitModel>		habit;
    private TaxonLookupModel		lookup;
	private List<DistributionModel>		distribution;
	private List<VernacularNameModel>	vernacularnames;
	private List<TaxonModel>			children;
	private List<TaxonModel>			parents;
	private List<TaxonModel>			hybridparents;
	

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return the uninomial
	 */
	public String getUninomial() {
		return uninomial;
	}

	/**
	 * @param uninomial the uninomial to set
	 */
	public void setUninomial(String uninomial) {
		this.uninomial = uninomial;
	}

	/**
	 * @return the binominal
	 */
	public String getBinomial() {
		return binomial;
	}

	/**
	 * @param binominal the binominal to set
	 */
	public void setBinomial(String binomial) {
		this.binomial = binomial;
	}

	/**
	 * @return the trinominal
	 */
	public String getTrinomial() {
		return trinomial;
	}

	/**
	 * @param trinominal the trinominal to set
	 */
	public void setTrinomial(String trinomial) {
		this.trinomial = trinomial;
	}

	/**
	 * @return the quarinomial
	 */
	public String getQuadrinomial() {
		return quadrinomial;
	}

	/**
	 * @param quarinomial the quarinomial to set
	 */
	public void setQuadrinomial(String quadrinomial) {
		this.quadrinomial = quadrinomial;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the rank
	 */
	@ManyToOne
	@JoinColumn(name="rankid")
	public RankModel getRank() {
		return rank;
	}

	/**
	 * @param rank the rank to set
	 */
	public void setRank(RankModel rank) {
		this.rank = rank;
	}
	
	@ManyToOne
	@JoinColumn(name="statusid")
	public StatusModel getStatus() {
		return status;
	}
	public void setStatus(StatusModel status) {
		this.status = status;
	}
	
	
	/**
	 * @param cdate
	 */
	public void setCdate(Date cdate) {
		this.cdate = cdate;
	}

	/**
	 * @return the modification date
	 */
	public Date getMdate() {
		return mdate;
	}

	/**
	 * @param mdate
	 */
	public void setMdate(Date mdate) {
		this.mdate = mdate;
	}

	/**
	 * @return the vernacularNames
	 */

	@OneToMany(mappedBy="taxon")
	@OrderBy("status ASC name ASC")
	public List<VernacularNameModel> getVernacularnames() {
		return vernacularnames;
	}
	public void setVernacularnames(List<VernacularNameModel> vernacularnames) {
		this.vernacularnames = vernacularnames;
	}
	
	/**
	 * Get the object's taxon concept number. The taxon number is a legacy 
	 * number mapped to different distribution files (xls, word, 
	 * filemakerpro) and seldom used by Luc Brouillet and his team.
	 * 
	 * @return the object's noTaxon. Function returns 0 if noTaxon is not set.
	 */	
	public Integer getNotaxon(){
		return notaxon;
	}
	
	/**
	 * Set the object's noTaxon.
	 * 
	 * @param noTaxon the object's noTaxon.
	 */
	public void setNotaxon(Integer notaxon){
		this.notaxon = notaxon;	
	}

	/**
	 * Get the object's reference.
	 * Reference indicates where the taxon concept is explicitly expressed.
	 * 
	 * @return the object's reference id. Function returns null if reference is 
	 * not set.
	 */
	@ManyToOne
	@JoinColumn(name="referenceid")
	public ReferenceModel getReference(){
		return reference;
	}
	
	/**
	 * Set the object's reference.
	 * 
	 * @param referenceId the object's reference.
	 */
	public void setReference(ReferenceModel reference){
		this.reference = reference;
	}

	/**
	 * Get the object's commentary. 
	 * 
	 * @return the object's commentary. Function returns null if commentary is
	 * not set.
	 */
	public String getCommentary(){
		return commentary;
	}
	
	/**
	 * Set the object's commentary.
	 * 
	 * @param commentary the object's commentary.
	 */
	public void setCommentary(String commentary){
		this.commentary = commentary;
	}

	/**
	 * @param notaxon the notaxon to set
	 */
	public void setNotaxon(int notaxon) {
		this.notaxon = notaxon;
	}

	/**
	 * @return the habits
	 */
	@ManyToMany
	@JoinTable(name = "taxonhabit",
	    joinColumns = {
			@JoinColumn(name="taxonid")
	    },inverseJoinColumns = {
			@JoinColumn(name="habitid")
	    }
	)
	@Basic(fetch = FetchType.LAZY)
	public List<HabitModel> getHabit() {
		return habit;
	}
	
	/**
	 * @param habitus the habitus to set
	 */
	public void setHabit(List<HabitModel> habit) {
		this.habit = habit;
	}

	/**
	 * Get the object's distributionlookup
	 * distributionlookup 
	 * 
	 * @return the object's distribution information (statusid for all provinces) 
	 */
	@NotFound(action=NotFoundAction.IGNORE)
	@OneToOne
	@JoinColumn(name="id",insertable=false,updatable=false,nullable=true)
	public TaxonLookupModel getLookup() {
		return lookup;
	}
	
	/**
	 * @param distributionlookup the distributionlookup to set
	 */
	public void setLookup(TaxonLookupModel lookup) {
		this.lookup = lookup;
	}
	
	/**
	 * @return the distribution
	 */
	@ManyToMany
	@JoinTable(name = "distribution",
		joinColumns = {
			@JoinColumn(name="taxonid")
	    },inverseJoinColumns = {
			@JoinColumn(name="id")
    	}
	)
	@Basic(fetch = FetchType.LAZY)
	public List<DistributionModel> getDistribution() {
		return distribution;
	}

	/**
	 * @param distribution the distribution to set
	 */
	public void setDistribution(List<DistributionModel> distribution) {
		this.distribution = distribution;
	}
	

	/**
	 * The list is sorted by calname. This list is not sorted since the calname
	 * is not available in taxon table for an ORDER BY and the hibernate sort only accept Set and Map.
	 * @return the children
	 */
	@ManyToMany
	@JoinTable(name = "taxonomy",
	    joinColumns = {
			@JoinColumn(name="parentid",insertable=false,updatable=false)
	    },inverseJoinColumns = {
			@JoinColumn(name="childid",insertable=false,updatable=false)
	    }
	)
	@Basic(fetch = FetchType.LAZY)
	public List<TaxonModel> getChildren() {
		return children;
	}
	
	/**
	 * @param children the children to set
	 */
	public void setChildren(List<TaxonModel> children) {
		this.children = children;
	}
	
	/**
	 * @return the hybridParents
	 */
	@ManyToMany
	@JoinTable(name = "taxonhybridparent",
	    joinColumns = {
			@JoinColumn(name="childid")
	    }
		,inverseJoinColumns = {
			@JoinColumn(name="parentid")
	    }
	)
	@Basic(fetch = FetchType.LAZY)
	public List<TaxonModel> getHybridparents() {
		return hybridparents;
	}
	
	/**
	 * @param hybridparents the hybrid parents to set
	 */
	public void setHybridparents(List<TaxonModel> hybridparents) {
		this.hybridparents = hybridparents;
	}
	
	/**
	 * @return the parents
	 */
	@ManyToMany
	@JoinTable(name = "taxonomy",
	    joinColumns = {
			@JoinColumn(name="childid")
	    },inverseJoinColumns = {
			@JoinColumn(name="parentid")
	    }
	)
	public List<TaxonModel> getParents() {
		return parents;
	}

	/**
	 * @param parents the parents to set
	 */
	public void setParents(List<TaxonModel> parents) {
		this.parents = parents;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		//TODO : $$EnhancerByCGLIB$$6c2b56f0 added to class name...
		// check hibernate.cfg.xml hibernate.hibernate.cglib.use_reflection_optimizer ... which might 
		// cause this.
		/*if (getClass() != obj.getClass())
			return false;
		*/
		TaxonModel other = (TaxonModel) obj;
		if (this.getId() != other.getId())
			return false;
		
		return true;
	}
	
	
	@Override
	/**
	 * toString is mainly used in the admin backend, where a basic string representation
	 * is dumped to the logfile, in a before/after fashion (changed from / changed to) 
	 */
	public String toString() {
		String delimiter = " ";
		//String newline = "\n";
		StringBuffer taxon = new StringBuffer("");
		taxon.append(this.id).append(delimiter);
		taxon.append(this.uninomial).append(delimiter);
		taxon.append(this.binomial).append(delimiter);
		taxon.append(this.trinomial).append(delimiter);
		taxon.append(this.quadrinomial).append(delimiter);
		taxon.append(this.author).append(delimiter);
		taxon.append(this.commentary).append(delimiter);
		taxon.append(this.rank.getRank()).append(delimiter);
		taxon.append(this.status.getStatus()).append(delimiter);
		taxon.append(this.reference.getReferencecode());
		return taxon.toString();
	}
	
	/**
	 * Compare based on the lookup calculated name
	 */
	public int compareTo(TaxonModel obj){
		try{
			if(obj != null && obj.getLookup() != null && this.lookup != null){
				return this.lookup.getCalname().compareTo(obj.getLookup().getCalname());
			}
			else
				return 1;
			}
		catch(Exception e){
			return 1;
		}
	}
	
	public static class CompareLookup implements Comparator<Object>{
		@Override
		public int compare(Object arg0, Object arg1) {
			if(arg0 == null || arg1 == null)
				return 0;
			TaxonModel obj1 = (TaxonModel)arg0;
			TaxonModel obj2 = (TaxonModel)arg1;
			return obj1.compareTo(obj2);
		}
	}
}
