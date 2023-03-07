package com.srecko.reddit.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String identifier) {
        super(identifier.contains("@") ? "User with email " + identifier + " is not found." : "User with username " + identifier + " is not found.");
    }

    public UserNotFoundException(Long id) {
        super("User with id " + id + " is not found");
    }
}
