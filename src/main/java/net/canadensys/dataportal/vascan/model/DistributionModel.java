/*
	Copyright (c) 2010 Canadensys
*/
package net.canadensys.dataportal.vascan.model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Model to hold information about the distribution of a taxon.
 * @author canadensys
 *
 */
@Entity
@Table(name="distribution")
public class DistributionModel{
	
	private int					id;
	private TaxonModel			taxon;
	private RegionModel			region;
	private DistributionStatusModel	distributionStatus;
	private ExcludedCodeModel	excludedcode;
	private ReferenceModel		reference;
	
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	
	/**
	 * @param distributionid the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	
	/**
	 * @return the taxon
	 */
	@ManyToOne
	@JoinColumn(name="taxonid")
	public TaxonModel getTaxon() {
		return taxon;
	}
	public void setTaxon(TaxonModel taxon) {
		this.taxon = taxon;
	}
	

	@ManyToOne
	@JoinColumn(name="regionid")
	public RegionModel getRegion() {
		return region;
	}
	public void setRegion(RegionModel region) {
		this.region = region;
	}
	
	/**
	 * @return the distributionStatus
	 */
	@ManyToOne
	@JoinColumn(name="distributionstatusid")
	public DistributionStatusModel getDistributionStatus() {
		return distributionStatus;
	}
	
	/**
	 * @param distributionStatus the distributionStatus to set
	 */
	public void setDistributionStatus(DistributionStatusModel distributionStatus) {
		this.distributionStatus = distributionStatus;
	}
	
	/**
	 * @return the excludedcode
	 */
	@ManyToOne
	@JoinColumn(name="excludedcodeid")
	public ExcludedCodeModel getExcludedcode() {
		return excludedcode;
	}
	
	/**
	 * @param excludedcode the excludedcode to set
	 */
	public void setExcludedcode(ExcludedCodeModel excludedcode) {
		this.excludedcode = excludedcode;
	}


	@ManyToOne
	@JoinColumn(name="referenceid")
	public ReferenceModel getReference() {
		return reference;
	}
	public void setReference(ReferenceModel reference) {
		this.reference = reference;
	}
	
	@Override
	/**
	 * toString is mainly used in the admin backend, where a basic string representation
	 * is dumped to the logfile, in a before/after fashion (changed from / changed to) 
	 */
	public String toString() {
		String delimiter = " ";
		//String newline = "\n";
		StringBuffer distribution = new StringBuffer("");
		distribution.append(this.id).append(delimiter);
		distribution.append(this.taxon.getLookup().getCalname()).append(delimiter);
		distribution.append(this.getDistributionStatus().getDistributionstatus()).append(delimiter);
		distribution.append(this.getExcludedcode().getExcludedcode()).append(delimiter);
		distribution.append(this.getReference().getReferenceshort());
		return distribution.toString();
	}	
}