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
	private String resource_uuid;
	private String archive_url;
	private String sourcefileid;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getResource_uuid() {
		return resource_uuid;
	}
	public void setResource_uuid(String resource_uuid) {
		this.resource_uuid = resource_uuid;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
