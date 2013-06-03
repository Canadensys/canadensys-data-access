/*
	Copyright (c) 2010-2013 Canadensys
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

@Entity
@Table(name="reference")
public class ReferenceModel {
	
	private int			id;
	private String		referencecode;
	private String		referenceshort;
	private String		reference;
	private	String		url;

	@Temporal(TemporalType.TIMESTAMP)
	private	Date		cdate;
		
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getReferencecode() {
		return referencecode;
	}
	public void setReferencecode(String referencecode) {
		this.referencecode = referencecode;
	}

	public String getReferenceshort() {
		return referenceshort;
	}
	public void setReferenceshort(String referenceshort) {
		this.referenceshort = referenceshort;
	}
	
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public Date getCdate() {
		return cdate;
	}
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
