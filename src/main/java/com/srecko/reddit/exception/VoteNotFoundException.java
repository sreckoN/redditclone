package com.srecko.reddit.exception;

public class VoteNotFoundException extends RuntimeException {

    public VoteNotFoundException(Long id) {
        super("Vote with id " + id + " is not found.");
    }
}
