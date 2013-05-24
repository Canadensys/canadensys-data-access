/*
	Copyright (c) 2010 Canadensys
*/
package net.canadensys.dataportal.vascan.model;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="vernacularname")
public class VernacularNameModel {
	
	private int				id;
	private String			name;
	private StatusModel		status;
	private TaxonModel		taxon;
	private String			language;
	private ReferenceModel 	reference;
	@Temporal(TemporalType.TIMESTAMP)
	private	Date			cdate;
	@Temporal(TemporalType.TIMESTAMP)
	private	Date			mdate;
	
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne
	@JoinColumn(name="statusid")
	public StatusModel getStatus() {
		return status;
	}

	/**
	 * @param status
	 */
	public void setStatus(StatusModel status) {
		this.status = status;
	}
	
	@ManyToOne
	@JoinColumn(name="taxonid")
	public TaxonModel getTaxon() {
		return taxon;
	}

	/**
	 * @param taxon
	 */

	public void setTaxon(TaxonModel taxon) {
		this.taxon = taxon;
	}
	
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}
	
	/**
	 * @param language
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	
	@ManyToOne
	@JoinColumn(name="referenceid")
	public ReferenceModel getReference() {
		return reference;
	}

	/**
	 * @param refrence
	 */
	public void setReference(ReferenceModel reference) {
		this.reference = reference;
	}

	/**
	 * @return the creation date
	 */
	public Date getCdate() {
		return cdate;
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
	
	@Override
	/**
	 * toString is mainly used in the admin backend, where a basic string representation
	 * is dumped to the logfile, in a before/after fashion (changed from / changed to) 
	 */
	public String toString() {
		String delimiter = " ";
		//String newline = "\n";
		StringBuffer lookup = new StringBuffer("");
		lookup.append(this.id).append(delimiter);
		lookup.append(this.name).append(delimiter);
		lookup.append(this.status).append(delimiter);
		lookup.append(this.taxon.getLookup().getCalname()).append(delimiter);
		lookup.append(this.getLanguage()).append(delimiter);	
		lookup.append(this.getReference().getReferenceshort());
		return lookup.toString();
	}	
}
