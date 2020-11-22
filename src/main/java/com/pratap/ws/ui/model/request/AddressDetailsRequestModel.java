package com.pratap.ws.ui.model.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.pratap.ws.shared.AddressType;

public class AddressDetailsRequestModel {

	@NotNull(message="houseNo is a required field")
    @Size(min=3, max=16, message="houseNo must be equal to or greater than 3 characters and less than 16 characters")
	private String houseNo;
	
	@NotNull(message="city is a required field")
    @Size(min=3, max=16, message="city must be equal to or greater than 3 characters and less than 16 characters")
	private String city;
	
	@NotNull(message="state is a required field")
    @Size(min=2, max=16, message="state must be equal to or greater than 3 characters and less than 16 characters")
	private String state;
	
	@NotNull(message="country is a required field")
    @Size(min=3, max=16, message="country must be equal to or greater than 3 characters and less than 16 characters")
	private String country;
	
	@NotNull(message="streetName is a required field")
    @Size(min=4, max=16, message="streetName must be equal to or greater than 4 characters and less than 16 characters")
	private String streetName;
	
	@NotNull(message="postalCode is a required field")
    @Size(min=6, max=6, message="postalCode must be equal to 6 characters")
	private String postalCode;
	
	@NotNull(message="addressType(BILLING/SHIPPING) is a required field")
	private AddressType addressType;

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

}
