package com.pratap.ws.ui.model.response;

import java.util.List;

public class UserDetailsResponseModel {

	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	
	private List<AddressDetailsResponseModel> addresses;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public UserDetailsResponseModel() {}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<AddressDetailsResponseModel> getAddresses() {
		return addresses;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAddresses(List<AddressDetailsResponseModel> addresses) {
		this.addresses = addresses;
	}

}
