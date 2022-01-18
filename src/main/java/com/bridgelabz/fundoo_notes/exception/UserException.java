package com.bridgelabz.fundoo_notes.exception;

public class UserException extends RuntimeException{

    String message;

    public UserException(String message) {
        this.message = message;
    }
}
