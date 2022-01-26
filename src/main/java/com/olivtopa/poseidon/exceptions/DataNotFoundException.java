package com.olivtopa.poseidon.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DataNotFoundException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8946239564025155080L;

	public DataNotFoundException(String error) {
		super(error);
	}

}
