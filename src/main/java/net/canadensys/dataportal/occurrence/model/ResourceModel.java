package net.canadensys.dataportal.occurrence.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Model to keep info about resources. Resource represents the source archive.
 * @author canadensys
 *
 */
@Entity
@Table(name = "resource_management")
@SequenceGenerator(name = "resource_management_id_seq", sequenceName = "resource_management_id_seq", allocationSize=1)
public class ResourceModel {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resource_management_id_seq")
	private Integer id;
	private String name;
	private String key;
	private String archive_url;
	private String sourcefileid;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getArchive_url() {
		return archive_url;
	}
	public void setArchive_url(String archive_url) {
		this.archive_url = archive_url;
	}
	public String getSourcefileid() {
		return sourcefileid;
	}
	public void setSourcefileid(String sourcefileid) {
		this.sourcefileid = sourcefileid;
	}
}
