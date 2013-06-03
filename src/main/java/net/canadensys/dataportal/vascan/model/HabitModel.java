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
 * Model representing a habit (tree, shrub, ...)
 * @author canadensys
 *
 */
@Entity
@Table(name="habit")
public class HabitModel{
	private int			id;
	private String		habit;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return the habit name
	 */
	public String getHabit() {
		return habit;
	}

	/**
	 * @param habit the habit name
	 */
	public void setHabit(String habit) {
		this.habit = habit;
	}
}
