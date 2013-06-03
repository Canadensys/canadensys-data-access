/*
	Copyright (c) 2010 Canadensys
*/
package net.canadensys.dataportal.vascan.model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Model representing a status of a distribution(native, introduced, ...)
 * @author canadensys
 *
 */
@Entity
@Table(name="distributionstatus")
public class DistributionStatusModel{

	private int				id;
	private String			distributionstatus;
	private String			occurrencestatus;
	private String			establishmentmeans;
	

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return the distributionstatus
	 */
	public String getDistributionstatus() {
		return distributionstatus;
	}
	
	/**
	 * @param distributionstatus the distributionstatus to set
	 */
	public void setDistributionstatus(String distributionstatus) {
		this.distributionstatus = distributionstatus;
	}
	
	/**
	 * @return the occurencestatus
	 */
	public String getOccurrencestatus() {
		return occurrencestatus;
	}
	
	/**
	 * @param occurencestatus the occurencestatus to set
	 */
	public void setOccurrencestatus(String occurrencestatus) {
		this.occurrencestatus = occurrencestatus;
	}
	
	/**
	 * @return the establishmentmeans
	 */
	public String getEstablishmentmeans() {
		return establishmentmeans;
	}
	
	/**
	 * @param establishmentmeans the establishmentmeans to set
	 */
	public void setEstablishmentmeans(String establishmentmeans) {
		this.establishmentmeans = establishmentmeans;
	}
}
