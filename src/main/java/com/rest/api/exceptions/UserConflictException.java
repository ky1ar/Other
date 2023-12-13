package com.rest.api.exceptions;

public class UserConflictException extends RuntimeException {

    public UserConflictException(String email) {
        super("Customer with email " + email + " already exists.");
    }
}
