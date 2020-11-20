package com.pratap.ws.exceptions;

public class UserDetailsServiceException extends RuntimeException {

	private static final long serialVersionUID = 4053283963305780591L;

	public UserDetailsServiceException(String message) {
		super(message);
	}
}
