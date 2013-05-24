/*
	Copyright (c) 2010 Canadensys
*/
package net.canadensys.dataportal.vascan.model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="excludedcode")
public class ExcludedCodeModel {
	
	private int			id;
	private String		excludedcode;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getExcludedcode() {
		return excludedcode;
	}
	public void setExcludedcode(String excludedcode) {
		this.excludedcode = excludedcode;
	}
}