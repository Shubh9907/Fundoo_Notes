package com.bridgelabz.fundoo_notes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bridgelabz.fundoo_notes.exception.UserException;
import com.bridgelabz.fundoo_notes.utility.ApiResponse;

@ControllerAdvice
public class ExceptionController {
	
    ApiResponse userNotFound = new ApiResponse("User Not Found", 601, null);
    ApiResponse noteNotFound = new ApiResponse("Note Not Found", 601, null);

	@ExceptionHandler(value = UserException.class)
	public ResponseEntity<ApiResponse> exception(UserException exception) {
	      return new ResponseEntity<ApiResponse>(userNotFound, HttpStatus.OK);
	}
	
	@ExceptionHandler(value = NoteException.class)
	public ResponseEntity<ApiResponse> noteException(NoteException exception) {
	      return new ResponseEntity<ApiResponse>(noteNotFound, HttpStatus.OK);
	}
}
