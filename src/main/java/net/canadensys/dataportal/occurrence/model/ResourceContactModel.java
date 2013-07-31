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
	
	private String dataset_shortname;
	private String dataset_title;
	
	private String name;
	private String position_name;
	private String organization_name;
	private String address;
	private String city;
	private String administrative_area;
	private String country;
	private String postal_code;
	
	private String phone;
	private String email;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getDataset_shortname() {
		return dataset_shortname;
	}
	public void setDataset_shortname(String dataset_shortname) {
		this.dataset_shortname = dataset_shortname;
	}
	
	public String getDataset_title() {
		return dataset_title;
	}
	public void setDataset_title(String dataset_title) {
		this.dataset_title = dataset_title;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPosition_name() {
		return position_name;
	}
	public void setPosition_name(String position_name) {
		this.position_name = position_name;
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
	public void setAdministrative_area(String administrative_area) {
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
