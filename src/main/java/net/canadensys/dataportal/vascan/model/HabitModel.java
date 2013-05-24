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
@Table(name="habit")
public class HabitModel{
	private int			id;
	private String		habit;

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
	 * @return the habitus
	 */
	public String getHabit() {
		return habit;
	}

	/**
	 * @param habituscode the habitus to set
	 */
	public void setHabit(String habit) {
		this.habit = habit;
	}
}
