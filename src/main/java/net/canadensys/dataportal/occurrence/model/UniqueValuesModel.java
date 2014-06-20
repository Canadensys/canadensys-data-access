package net.canadensys.dataportal.occurrence.model;

/**
 * Model to hold preprocessed unique values.
 * @author canadensys
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import net.canadensys.model.SuggestedValue;

@Entity
@Table(name = "unique_values")
public class UniqueValuesModel extends SuggestedValue{
	
	private int id;
	
	private String key;
	private String unaccented_value;
	
	@Id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "key")
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	/**
	 * setCount alias to comply with JavaBean convention
	 * @param count
	 */
	public void setOccurrence_count(int count) {
		setCount(count);
	}
	
	/**
	 * getCount alias to comply with JavaBean convention
	 * @return
	 */
	@Column(name = "occurrence_count")
	public int getOccurrence_count(){
		return (int)getCount();
	}
	
	@Override
	public void setValue(String value) {
		this.value = value;
	}
	@Column(name = "value")
	@Override
	public String getValue() {
		return value;
	}
	
	@Column(name = "unaccented_value")
	public String getUnaccented_value() {
		return unaccented_value;
	}
	public void setUnaccented_value(String unaccented_value) {
		this.unaccented_value = unaccented_value;
	}
}
