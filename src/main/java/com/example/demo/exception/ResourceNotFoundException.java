package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends UsernameNotFoundException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -539378475930741220L;

	private static final String EXCEPTION_MESSAGE = "Resource not found with ID : %s";
	private static final String MESSAGE = "Resource not found";

	public ResourceNotFoundException() {
		super(MESSAGE);
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(Long id) {
		super(String.format(EXCEPTION_MESSAGE, id));
	}

}
