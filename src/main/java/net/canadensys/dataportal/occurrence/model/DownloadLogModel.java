package net.canadensys.dataportal.occurrence.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Model to track the download of search results
 * @author canadensys
 */
@Entity
@Table(name = "download_log")
//allocationSize=1 is a workaround to avoid improper behavior
//http://acodapella.blogspot.ca/2011/06/hibernate-annotation-postgresql.html
@SequenceGenerator(name = "download_log_id_seq", sequenceName = "download_log_id_seq", allocationSize=1)
public class DownloadLogModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "download_log_id_seq")
	private Integer id;
	private Date event_date;
	private String search_criteria;
	private Integer number_of_records;
	private String email;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getEvent_date() {
		return event_date;
	}
	public void setEvent_date(Date event_date) {
		this.event_date = event_date;
	}
	public String getSearch_criteria() {
		return search_criteria;
	}
	public void setSearch_criteria(String search_criteria) {
		this.search_criteria = search_criteria;
	}
	public Integer getNumber_of_records() {
		return number_of_records;
	}
	public void setNumber_of_records(Integer number_of_records) {
		this.number_of_records = number_of_records;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
