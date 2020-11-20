package com.pratap.ws.service;

import java.util.List;

import com.pratap.ws.shared.dto.UserDetailsDTO;

public interface UserDetailsService {

	UserDetailsDTO getUserByUserId(String userId);
	
	UserDetailsDTO getUserByEmail(String email);
	
	UserDetailsDTO createUser(UserDetailsDTO userDetails);
	
	List<UserDetailsDTO> getusers();
	
	void deleteUser(String userId);
	
	UserDetailsDTO updateUser(UserDetailsDTO updateUserDetails, String userId);
	
	UserDetailsDTO updatePartialUserDetails(UserDetailsDTO partialUserDetails, String userId);

}
