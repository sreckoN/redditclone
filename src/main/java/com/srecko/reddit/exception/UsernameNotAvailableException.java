package com.srecko.reddit.exception;

public class UsernameNotAvailableException extends RuntimeException {

    public UsernameNotAvailableException(String username) {
        super("Username " + username + " is already in use.");
    }
}
