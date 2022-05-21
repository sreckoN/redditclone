package com.srecko.reddit.exception;

public class EmailAlreadyInUseException extends RuntimeException {

    public EmailAlreadyInUseException(String email) {
        super("Email " + email + " is already in use");
    }
}
