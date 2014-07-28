package net.canadensys.dataportal.occurrence.model;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * Model containing data related to a DarwinCore occurrence extension. Data are stored in a key/value map allowing to abstract specific extension columns.
 * @author cgendreau
 *
 */
@Entity
@Table(name = "occurrence_extension")
public class OccurrenceExtensionModel {
	
	@Id
	private int id;
	
	private String ext_type;
	private String ext_version;
	
	@Type(type="net.canadensys.databaseutils.hibernate.KeyValueType")
	private Map<String,String> ext_data;
		
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getExt_type() {
		return ext_type;
	}
	public void setExt_type(String ext_type) {
		this.ext_type = ext_type;
	}
	
	public String getExt_version() {
		return ext_version;
	}
	public void setExt_version(String ext_version) {
		this.ext_version = ext_version;
	}
	
	public Map<String,String> getExt_data() {
		return ext_data;
	}
	public void setExt_data(Map<String,String> ext_data) {
		this.ext_data = ext_data;
	}

}
