package net.canadensys.dataportal.occurrence.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * Model to track the import of DwcA
 * @author canadensys
 */
@Entity
@Table(name = "import_log")
//allocationSize=1 is a workaround to avoid improper behavior
//http://acodapella.blogspot.ca/2011/06/hibernate-annotation-postgresql.html
@SequenceGenerator(name = "import_log_id_seq", sequenceName = "import_log_id_seq", allocationSize=1)
public class ImportLogModel implements Serializable{
	
	private static final long serialVersionUID = 8675980642316964256L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "import_log_id_seq")
	private Integer id;
	private String sourcefileid;
	private Integer record_quantity;
	private String updated_by;
	private Integer import_process_duration_ms;
	private Date event_end_date_time;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSourcefileid() {
		return sourcefileid;
	}
	public void setSourcefileid(String sourcefileid) {
		this.sourcefileid = sourcefileid;
	}
	public Integer getRecord_quantity() {
		return record_quantity;
	}
	public void setRecord_quantity(Integer record_quantity) {
		this.record_quantity = record_quantity;
	}
	public String getUpdated_by() {
		return updated_by;
	}
	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}
	public Integer getImport_process_duration_ms() {
		return import_process_duration_ms;
	}
	public void setImport_process_duration_ms(Integer import_process_duration_ms) {
		this.import_process_duration_ms = import_process_duration_ms;
	}
	public Date getEvent_end_date_time() {
		return event_end_date_time;
	}
	public void setEvent_end_date_time(Date event_end_date_time) {
		this.event_end_date_time = event_end_date_time;
	}
	
	public String toString() {
		return new ToStringBuilder(this).
	       append("id", id).
	       append("sourceFileId", sourcefileid).
	       append("recordQuantity", record_quantity).
	       append("updated_by", updated_by).
	       toString();
	 }
}