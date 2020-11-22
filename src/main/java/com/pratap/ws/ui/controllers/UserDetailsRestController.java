package com.pratap.ws.ui.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pratap.ws.exceptions.UserDetailsServiceException;
import com.pratap.ws.service.UserDetailsService;
import com.pratap.ws.shared.dto.AddressDetailsDTO;
import com.pratap.ws.shared.dto.UserDetailsDTO;
import com.pratap.ws.ui.model.request.UserDetailsRequestModel;
import com.pratap.ws.ui.model.request.UserDetailsUpdateRequestModel;
import com.pratap.ws.ui.model.response.AddressDetailsResponseModel;
import com.pratap.ws.ui.model.response.UserDetailsResponseModel;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;

@RestController
public class UserDetailsRestController {

	@Autowired
	private UserDetailsService userDetailsService;

	ModelMapper modelMapper;

	@ApiResponse(code = 200, message = "Found")
	@ApiOperation(value = "Fetch the user details by userId", notes = "Existing user details", response = UserDetailsResponseModel.class, responseContainer = "Object")
	@GetMapping(value = "/users/{userId}", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<UserDetailsResponseModel> getUserByUserId(

			@ApiParam(name = "userId", type = "String", value = "String", required = true)

			@PathVariable("userId") String userId) {

		modelMapper = new ModelMapper();
		UserDetailsDTO userDetailsDTO = userDetailsService.getUserByUserId(userId);
		UserDetailsResponseModel response = modelMapper.map(userDetailsDTO, UserDetailsResponseModel.class);

		if (response != null) {
			return new ResponseEntity<UserDetailsResponseModel>(response, HttpStatus.OK);
		}
		return new ResponseEntity<UserDetailsResponseModel>(HttpStatus.NOT_FOUND);
	}

	@ApiResponse(code = 200, message = "Found")
	@ApiOperation(value = "Search the user details by email", notes = "Existing user details", response = UserDetailsResponseModel.class, responseContainer = "Object")
	@GetMapping( path = "/users/searchBy",
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<UserDetailsResponseModel> getUserByEmail(
			
			@ApiParam(name = "email", type = "String", value = "String", required = true)
			
			@RequestParam(value = "email") String email) throws UserDetailsServiceException{

		if(!email.isEmpty()) {
			
			modelMapper = new ModelMapper();
			UserDetailsDTO userDetailsDTO = userDetailsService.getUserByEmail(email);
			UserDetailsResponseModel response = modelMapper.map(userDetailsDTO, UserDetailsResponseModel.class);
			
			if(response != null) {
				return new ResponseEntity<UserDetailsResponseModel>(response, HttpStatus.OK);
			}
		}
		return new ResponseEntity<UserDetailsResponseModel>(HttpStatus.BAD_REQUEST);
		
	}

	@ApiResponse(code = 201, message = "created")
	@ApiOperation(value = "create a new entry of UserDetailsEntity in to DB", notes = "A new userdetails data record for specific User", response = UserDetailsResponseModel.class, responseContainer = "Object")

	@PostMapping(path = "/users", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public UserDetailsResponseModel createUser(
			@ApiParam(name = "UserDetailsRequestModel", type = "Object", value = "UserDetailsRequestModel", required = true)

			@Valid @RequestBody UserDetailsRequestModel requestModel) {

		modelMapper = new ModelMapper();
		UserDetailsDTO userDetailsDTO = modelMapper.map(requestModel, UserDetailsDTO.class);

		return modelMapper.map(userDetailsService.createUser(userDetailsDTO), UserDetailsResponseModel.class);
	}

	@ApiResponse(code = 200, message = "Found")
	@ApiOperation(value = "Fetch the all user details", notes = "Existing users details", response = List.class, responseContainer = "Object")
	@GetMapping(path = "/users", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<List<UserDetailsResponseModel>> getUsers() {

		modelMapper = new ModelMapper();

		return new ResponseEntity<>(userDetailsService.getusers().stream()
				.map(dto -> modelMapper.map(dto, UserDetailsResponseModel.class)).collect(Collectors.toList()),
				HttpStatus.OK);
	}

	@ApiResponse(code = 202, message = "Updated")
	@ApiOperation(value = "Update the existing user details by UserDetailsUpdateRequestModel", notes = "Existing user details", response = UserDetailsResponseModel.class, responseContainer = "Object")
	@PutMapping(path = "/users/{userId}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserDetailsResponseModel> updateUserDetails(
			@ApiParam(name = "UserDetailsUpdateRequestModel", type = "com.pratap.ws.ui.model.request.UserDetailsUpdateRequestModel", value = "UserDetailsUpdateRequestModel", required = true)
			@RequestBody UserDetailsUpdateRequestModel updateRequest, 
			
			@ApiParam(name = "userId", type = "String", value = "userId", required = true)
			@PathVariable("userId") String userId) {

		modelMapper = new ModelMapper();
		UserDetailsDTO userDetailsUpdateDTO = modelMapper.map(updateRequest, UserDetailsDTO.class);
			UserDetailsDTO updatedUserDetailsDTO = userDetailsService.updateUser(userDetailsUpdateDTO, userId);
			UserDetailsResponseModel responseModel = modelMapper.map(updatedUserDetailsDTO, UserDetailsResponseModel.class);
			if (responseModel != null) {
				return new ResponseEntity<UserDetailsResponseModel>(responseModel, HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@ApiResponse(code = 203, message = "Deleted")
	@ApiOperation(value = "Delete the existing user details by UserId", notes = "Delete an Existing user details", response = Void.class, responseContainer = "Object")
	@DeleteMapping("/users/{userId}")
	public ResponseEntity<Void> deleteUser(
			@ApiParam(name = "userId", type = "String", value = "userId", required = true)
			@PathVariable("userId") String userId) {

		userDetailsService.deleteUser(userId);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
	@ApiResponse(code = 204, message = "Partially Update")
	@ApiOperation(value = "Partially update the existing user details by UserId", notes = "Partially update an Existing user details", response = UserDetailsResponseModel.class, responseContainer = "Object")
	@PatchMapping(path = "/users/{userId}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserDetailsResponseModel> updatePartialUserDetails(
			@ApiParam(name = "userId", type = "String", value = "userId", required = true)
			@PathVariable("userId") String userId,
			
			@ApiParam(name = "UserDetailsUpdateRequestModel", type = "UserDetailsUpdateRequestModel", value = "UserDetailsUpdateRequestModel", required = true)
			@RequestBody UserDetailsUpdateRequestModel updateRequest) {

		modelMapper = new ModelMapper();
		UserDetailsDTO userDetailsUpdateDTO = modelMapper.map(updateRequest, UserDetailsDTO.class);
		UserDetailsDTO updatedPartialUserDetailsDTO = userDetailsService.updatePartialUserDetails(userDetailsUpdateDTO, userId);
		UserDetailsResponseModel responseModel = modelMapper.map(updatedPartialUserDetailsDTO, UserDetailsResponseModel.class);
		if (responseModel != null) {
			return new ResponseEntity<UserDetailsResponseModel>(responseModel, HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@ApiResponse(code = 205, message = "Fetch the User's Addresses")
	@ApiOperation(value = "Fetch the user's Addresses by userId", notes = "Fetch the user's Addresses", response = List.class, responseContainer = "Object")
	@GetMapping(path = "/users/{userId}/addresses", 
			produces = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public CollectionModel<AddressDetailsResponseModel> getUserAddresses(
			@ApiParam(name = "userId", type = "String", value = "userId", required = true)
			@PathVariable("userId") String userId){
		
		modelMapper = new ModelMapper();
		List<AddressDetailsDTO> userAddresses = userDetailsService.fetchUserAddresses(userId);
		List<AddressDetailsResponseModel> response = userAddresses.stream().map(dto -> modelMapper.map(dto, AddressDetailsResponseModel.class)).collect(Collectors.toList());
		
		//Adding links to Embedded list of Addressses
		response.forEach(address -> {
			address.add(linkTo(methodOn(UserDetailsRestController.class).getUserAddress(userId, address.getAddressId())).withSelfRel());
		});
		
		Link userLink = linkTo(methodOn(UserDetailsRestController.class).getUserByUserId(userId)).withRel("user");
		
		Link selfLink = linkTo(methodOn(UserDetailsRestController.class).getUserAddresses(userId)).withSelfRel();
		
		return CollectionModel.of(response, userLink, selfLink);
	}
	
	@ApiResponse(code = 206, message = "Fetch the User's specific Address")
	@ApiOperation(value = "Fetch the user's Address by userId and addressId", notes = "Fetch the user's specifc Addresses", response = List.class, responseContainer = "Object")
	@GetMapping(path = "/users/{userId}/addresses/{addressId}",
			produces = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public EntityModel<AddressDetailsResponseModel> getUserAddress(
			@ApiParam(name = "userId", type = "String", value = "userId", required = true)
			@PathVariable("userId") String userId, 
			
			@ApiParam(name = "addressId", type = "String", value = "addressId", required = true)
			@PathVariable("addressId") String addressId){
		
		modelMapper = new ModelMapper();
		AddressDetailsResponseModel response = modelMapper.map(userDetailsService.fetchUserAddress(userId, addressId), AddressDetailsResponseModel.class);
		
		Link userLink = linkTo(methodOn(UserDetailsRestController.class).getUserByUserId(userId)).withRel("user");
		
		Link userAddressesLink = linkTo(methodOn(UserDetailsRestController.class).getUserAddresses(userId)).withRel("addresses");
		
		Link selfLink = linkTo(methodOn(UserDetailsRestController.class).getUserAddress(userId, addressId)).withSelfRel();
		
		return EntityModel.of(response, Arrays.asList(userLink, userAddressesLink, selfLink));
	}

}
