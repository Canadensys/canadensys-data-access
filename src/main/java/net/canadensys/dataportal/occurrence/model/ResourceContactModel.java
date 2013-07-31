package net.canadensys.dataportal.occurrence.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "resource_contact")
@SequenceGenerator(name = "resource_contact_id_seq", sequenceName = "resource_contact_id_seq", allocationSize=1)
public class ResourceContactModel {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "resource_contact_id_seq")
	private Integer id;
	private String sourcefileid;
	
	private String name;
	private String organization_name;
	private String address;
	private String city;
	private String administrative_area;
	private String country;
	private String postal_code;
	
	private String online_url;
	private String phone;
	private String email;
	
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
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrganization_name() {
		return organization_name;
	}
	public void setOrganization_name(String organization_name) {
		this.organization_name = organization_name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAdministrative_area() {
		return administrative_area;
	}
	public void setAdministrative_rea(String administrative_area) {
		this.administrative_area = administrative_area;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPostal_code() {
		return postal_code;
	}
	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}
	public String getOnline_url() {
		return online_url;
	}
	public void setOnline_url(String online_url) {
		this.online_url = online_url;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
