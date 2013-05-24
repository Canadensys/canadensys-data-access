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
@Table(name="region")
public class RegionModel{
	
	private int				id;
	private String			region;
	private	String			iso3166_1;
	private String			iso3166_2;
	private int				sort;
	
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
	 * @return the province
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * @param province the province to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * @return the iso3166_1
	 */
	public String getIso3166_1() {
		return iso3166_1;
	}
	/**
	 * @param iso3166_1 the iso3166_1 to set
	 */
	public void setIso3166_1(String iso3166_1) {
		this.iso3166_1 = iso3166_1;
	}
	
	/**
	 * @return the iso3166_2
	 */
	public String getIso3166_2() {
		return iso3166_2;
	}
	
	/**
	 * @param iso3166_2 the iso3166_2 to set
	 */
	public void setIso3166_2(String iso3166_2) {
		this.iso3166_2 = iso3166_2;
	}
	
	/**
	 * @return the sort
	 */
	public int getSort() {
		return sort;
	}
	
	/**
	 * @param regionsort the sort to set
	 */
	public void setSort(int sort) {
		this.sort = sort;
	}
	
}
