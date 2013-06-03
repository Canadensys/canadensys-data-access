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
 * Model representing a rank (genus, family, order, ...)
 * @author canadensys
 *
 */
@Entity
@Table(name="rank")
public class RankModel{
	private int			id;
	private	String		rank;
	private int			sort;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getSort(){
		return sort;
	}
	public void setSort(int sort){
		this.sort = sort;
	}
	
	/**
	 * 
	 * @return the rank name
	 */
	public String getRank(){
		return rank;
	}
	/**
	 * 
	 * @param rank the rank name
	 */
	public void setRank(String rank){
		this.rank = rank;
	}
}
