package com.pratap.ws.utils;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class WSUtil {

	private static final Random RANDOM = new Random();
	private static final String ALPHABET = "123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
	public static String generateUserId(int length) {
		return generateRandomString(length);
	}

	public static String generateAddressId(int length) {
		return generateRandomString(length);
	}
	private static String generateRandomString(int length) {
		
		StringBuilder returnValue = new StringBuilder();
		for(int i = 0 ; i < length; i++) {
			returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
		}
		
		return new String(returnValue);
	}
	
}
