package com.example.demo.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class DataBaseTransactionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3041457147926755841L;

	public DataBaseTransactionException() {
		super("Unable to process this request. Due to unexpected error occurred in Database Transction");
	}

	public DataBaseTransactionException(Long... id) {
		super("Unable to process this requested by Id" + id
				+ " Due to unexpected error occurred in Database Transction");
	}

	public DataBaseTransactionException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataBaseTransactionException(String message) {
		super(message);
	}

}
