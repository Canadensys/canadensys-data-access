/*
	Copyright (c) 2010-2013 Canadensys
*/
package net.canadensys.dataportal.vascan.model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Model representing a region (Ontario, Greenland, ...)
 * @author canadensys
 *
 */
@Entity
@Table(name="region")
public class RegionModel{
	
	private int				id;
	private String			region;
	private	String			iso3166_1;
	private String			iso3166_2;
	private int				sort;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}

	public String getIso3166_1() {
		return iso3166_1;
	}
	public void setIso3166_1(String iso3166_1) {
		this.iso3166_1 = iso3166_1;
	}
	
	public String getIso3166_2() {
		return iso3166_2;
	}
	public void setIso3166_2(String iso3166_2) {
		this.iso3166_2 = iso3166_2;
	}
	
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	
}
