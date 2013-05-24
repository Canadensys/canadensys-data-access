/*
	Copyright (c) 2010 Canadensys
*/
package net.canadensys.dataportal.vascan.model;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name="reference")
public class ReferenceModel {
	
	private int			id;
	private String		referencecode;
	private String		referenceshort;
	private String		reference;
	private	String		url;
	
	@Transient
	private String		error;
	
	@Temporal(TemporalType.TIMESTAMP)
	private	Date		cdate;
	
	@Transient
	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}
	
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
	 * @return the referencecode
	 */
	public String getReferencecode() {
		return referencecode;
	}

	/**
	 * @param referencecode the referencecode to set
	 */
	public void setReferencecode(String referencecode) {
		this.referencecode = referencecode;
	}

	/**
	 * @return the referenceShort
	 */
	public String getReferenceshort() {
		return referenceshort;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReferenceshort(String referenceshort) {
		this.referenceshort = referenceshort;
	}
	
	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * @return the cdate
	 */
	public Date getCdate() {
		return cdate;
	}
	/**
	 * @param cdate the cdate to set
	 */
	public void setCdate(Date cdate) {
		this.cdate = cdate;
	}


	@Override
	/**
	 * toString is mainly used in the admin backend, where a basic string representation
	 * is dumped to the logfile, in a before/after fashion (changed from / changed to) 
	 */
	public String toString() {
		String delimiter = " ";
		//String newline = "\n";
		StringBuffer reference = new StringBuffer("");
		reference.append(this.id).append(delimiter);
		reference.append(this.referencecode).append(delimiter);
		reference.append(this.referenceshort).append(delimiter);
		reference.append(this.reference).append(delimiter);
		reference.append(this.url);
		return reference.toString();
	}	
}
