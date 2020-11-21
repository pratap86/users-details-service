package com.pratap.ws.ui.controllers;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("users")
public class UserDetailsRestController {

	@Autowired
	private UserDetailsService userDetailsService;

	ModelMapper modelMapper;

	@RequestMapping(value = "/{userId}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<UserDetailsResponseModel> getUserByUserId(@PathVariable("userId") String userId) {

		modelMapper = new ModelMapper();
		UserDetailsDTO userDetailsDTO = userDetailsService.getUserByUserId(userId);
		UserDetailsResponseModel response = modelMapper.map(userDetailsDTO, UserDetailsResponseModel.class);

		if (response != null) {
			return new ResponseEntity<UserDetailsResponseModel>(response, HttpStatus.OK);
		}
		return new ResponseEntity<UserDetailsResponseModel>(HttpStatus.NOT_FOUND);
	}

	@GetMapping( path = "/searchBy",
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<UserDetailsResponseModel> getUserByEmail(@RequestParam(value = "email") String email) throws UserDetailsServiceException{

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

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserDetailsResponseModel createUser(@Valid @RequestBody UserDetailsRequestModel requestModel) {

		modelMapper = new ModelMapper();
		UserDetailsDTO userDetailsDTO = modelMapper.map(requestModel, UserDetailsDTO.class);

		return modelMapper.map(userDetailsService.createUser(userDetailsDTO), UserDetailsResponseModel.class);
	}

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<List<UserDetailsResponseModel>> getUsers() {

		modelMapper = new ModelMapper();

		return new ResponseEntity<>(userDetailsService.getusers().stream()
				.map(dto -> modelMapper.map(dto, UserDetailsResponseModel.class)).collect(Collectors.toList()),
				HttpStatus.OK);
	}

	@PutMapping(path = "/{userId}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserDetailsResponseModel> updateUserDetails(@PathVariable("userId") String userId,
			@RequestBody UserDetailsUpdateRequestModel updateRequest) {

		modelMapper = new ModelMapper();
		UserDetailsDTO userDetailsUpdateDTO = modelMapper.map(updateRequest, UserDetailsDTO.class);
		UserDetailsDTO updatedUserDetailsDTO = userDetailsService.updateUser(userDetailsUpdateDTO, userId);
		UserDetailsResponseModel responseModel = modelMapper.map(updatedUserDetailsDTO, UserDetailsResponseModel.class);
		if (responseModel != null) {
			return new ResponseEntity<UserDetailsResponseModel>(responseModel, HttpStatus.CREATED);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable("userId") String userId) {

		userDetailsService.deleteUser(userId);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
	@PatchMapping(path = "/{userId}", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserDetailsResponseModel> updatePartialUserDetails(@PathVariable("userId") String userId,
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
	
	@GetMapping(path = "/{userId}/addresses", 
			produces = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public CollectionModel<AddressDetailsResponseModel> getUserAddresses(@PathVariable("userId") String userId){
		
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
	
	@GetMapping(path = "/{userId}/addresses/{addressId}",
			produces = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public EntityModel<AddressDetailsResponseModel> getUserAddress(@PathVariable("userId") String userId, @PathVariable("addressId") String addressId){
		
		modelMapper = new ModelMapper();
		AddressDetailsResponseModel response = modelMapper.map(userDetailsService.fetchUserAddress(userId, addressId), AddressDetailsResponseModel.class);
		
		Link userLink = linkTo(methodOn(UserDetailsRestController.class).getUserByUserId(userId)).withRel("user");
		
		Link userAddressesLink = linkTo(methodOn(UserDetailsRestController.class).getUserAddresses(userId)).withRel("addresses");
		
		Link selfLink = linkTo(methodOn(UserDetailsRestController.class).getUserAddress(userId, addressId)).withSelfRel();
		
		return EntityModel.of(response, Arrays.asList(userLink, userAddressesLink, selfLink));
	}

}
