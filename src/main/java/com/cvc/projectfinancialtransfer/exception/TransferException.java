package com.cvc.projectfinancialtransfer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TransferException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -289762257104136883L;
	
	private String description;

	public TransferException() {}
	
	public TransferException(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
}
