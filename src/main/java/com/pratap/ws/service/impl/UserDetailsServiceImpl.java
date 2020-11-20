package com.pratap.ws.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pratap.ws.dao.entities.UserDetailsEntity;
import com.pratap.ws.dao.repositories.UserDetailsRepository;
import com.pratap.ws.exceptions.UserDetailsServiceException;
import com.pratap.ws.service.UserDetailsService;
import com.pratap.ws.shared.dto.UserDetailsDTO;
import com.pratap.ws.utils.WSUtil;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserDetailsRepository userDetailsRepository;
	
	private ModelMapper modelmapper;

	@Override
	public UserDetailsDTO getUserByUserId(String userId) {
		
		modelmapper = new ModelMapper();
		UserDetailsEntity userDetailsEntity = userDetailsRepository.findByUserId(userId).orElseThrow(() -> new UserDetailsServiceException("User Not Found by ID: "+userId));
		
		return modelmapper.map(userDetailsEntity, UserDetailsDTO.class);
	}

	@Override
	public UserDetailsDTO getUserByEmail(String email) {
		
		modelmapper = new ModelMapper();
		UserDetailsEntity userDetailsEntity = userDetailsRepository.findByEmail(email).orElseThrow(() -> new UserDetailsServiceException("User Not Found by email: "+email));
		
		return modelmapper.map(userDetailsEntity, UserDetailsDTO.class);
	}
	
	@Override
	public UserDetailsDTO createUser(UserDetailsDTO userDetails) {

		logger.info("Inside UserDetailsServiceImpl.createUser() ", userDetails);
		modelmapper = new ModelMapper();
		UserDetailsEntity userDetailsEntity = modelmapper.map(userDetails, UserDetailsEntity.class);
		userDetailsEntity.setUserId(WSUtil.generateUserId(10));
		
		userDetailsEntity.getAddresses().stream().forEach(address -> {
			address.setAddressId(WSUtil.generateAddressId(12));
			address.setUser(userDetailsEntity);
		});
		
		UserDetailsEntity savedUserDetails = userDetailsRepository.save(userDetailsEntity);
		return modelmapper.map(savedUserDetails, UserDetailsDTO.class);
	}

	@Override
	public List<UserDetailsDTO> getusers() {

		modelmapper = new ModelMapper();
		
		List<UserDetailsEntity> userDetails = userDetailsRepository.findAll();
		if(userDetails != null && !userDetails.isEmpty()) {
			return userDetails.stream().map(entity -> modelmapper.map(entity, UserDetailsDTO.class)).collect(Collectors.toList());
		}
		
		throw new UserDetailsServiceException("Users Data Not available");
	}

	@Override
	public void deleteUser(String userId) {
		UserDetailsEntity userDetailsEntity = userDetailsRepository.findByUserId(userId).orElseThrow(() -> new UserDetailsServiceException("User Not Found By Id :"+userId));
		
		userDetailsRepository.delete(userDetailsEntity);
	}

	@Override
	public UserDetailsDTO updateUser(UserDetailsDTO updateUserDetails, String userId) {
		
		modelmapper = new ModelMapper();
		return modelmapper.map(userDetailsRepository.findByUserId(userId).map(dto -> {
			dto.setFirstName(updateUserDetails.getFirstName());
			dto.setLastName(updateUserDetails.getLastName());
			dto.setEmail(updateUserDetails.getEmail());
			dto.setPassword(updateUserDetails.getPassword());
			return userDetailsRepository.save(dto);
		}).orElseThrow(() -> new UserDetailsServiceException("User Not Found By Id :"+userId)), UserDetailsDTO.class);
	}

	@Override
	public UserDetailsDTO updatePartialUserDetails(UserDetailsDTO partialUserDetails, String userId) {
		modelmapper = new ModelMapper();
		return modelmapper.map(userDetailsRepository.findByUserId(userId).map(dto -> {
			return partialUpdate(partialUserDetails, dto);
		}).orElseThrow(() -> new UserDetailsServiceException("User Not Found By Id :"+userId)), UserDetailsDTO.class);
	}

	private UserDetailsEntity partialUpdate(UserDetailsDTO partialUserDetails, UserDetailsEntity dto) {
		if (partialUserDetails.getFirstName() != null)
			dto.setFirstName(partialUserDetails.getFirstName());
		if (partialUserDetails.getLastName() != null)
			dto.setLastName(partialUserDetails.getLastName());
		if (partialUserDetails.getEmail() != null)
			dto.setEmail(partialUserDetails.getEmail());
		if (partialUserDetails.getPassword() != null)
			dto.setPassword(partialUserDetails.getPassword());
		return userDetailsRepository.save(dto);
	}

}
