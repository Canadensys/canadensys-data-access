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
@Table(name="rank")
public class RankModel{
	private int			id;
	private	String		rank;
	private int			sort;
	
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

	public int getSort(){
		return sort;
	}
	
	public void setSort(int sort){
		this.sort = sort;
	}
	
	public String getRank(){
		return rank;
	}
	
	public void setRank(String rank){
		this.rank = rank;
	}
}
