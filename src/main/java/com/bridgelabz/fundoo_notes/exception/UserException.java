package com.bridgelabz.fundoo_notes.exception;

import com.bridgelabz.fundoo_notes.utility.ApiResponse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserException extends RuntimeException {

	
	private static final long serialVersionUID = 1L;
	private ApiResponse response = new ApiResponse("User Not Found", 601, null);

	public UserException() {
		
	}
}
