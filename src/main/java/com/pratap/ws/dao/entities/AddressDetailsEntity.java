package com.pratap.ws.dao.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.pratap.ws.shared.AddressType;

@Entity(name = "addresses")
public class AddressDetailsEntity implements Serializable {
	
	
	private static final long serialVersionUID = -4603033115727544111L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "address_id")
	private String addressId;
	
	@Column(name = "house_no")
	private String houseNo;
	
	private String city;
	private String state;
	private String country;
	
	@Column(name = "street_name")
	private String streetName;
	
	@Column(name = "postal_code")
	private String postalCode;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "address_type")
	private AddressType addressType;
	
	@ManyToOne
	@JoinColumn(name = "users_id")
	private UserDetailsEntity user;
	
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	public String getHouseNo() {
		return houseNo;
	}
	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public AddressType getAddressType() {
		return addressType;
	}
	public void setAddressType(AddressType addressType) {
		this.addressType = addressType;
	}
	public UserDetailsEntity getUser() {
		return user;
	}
	public void setUser(UserDetailsEntity user) {
		this.user = user;
	}
	public Long getId() {
		return id;
	}
	
}
