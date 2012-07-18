package net.canadensys.dataportal.occurrence.model;

/**
 * Model to hold preprocessed unique values.
 * @author canadensys
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "unique_values")
public class UniqueValuesModel {
	
	@Id
	private int id;
	
	@Column(name = "key")
	private String key;
	
	@Column(name = "occurrence_count")
	private int occurrence_count;
	
	@Column(name = "value")
	private String value;

	@Column(name = "unaccented_value")
	private String unaccented_value;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public int getOccurrence_count() {
		return occurrence_count;
	}
	public void setOccurrence_count(int occurrence_count) {
		this.occurrence_count = occurrence_count;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getUnaccented_value() {
		return unaccented_value;
	}
	public void setUnaccented_value(String unaccented_value) {
		this.unaccented_value = unaccented_value;
	}
}
