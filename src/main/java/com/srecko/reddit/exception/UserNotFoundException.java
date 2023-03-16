package com.srecko.reddit.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String username) {
        super("User with username " + username + " is not found.");
    }

    public UserNotFoundException(Long id) {
        super("User with id " + id + " is not found");
    }
}
