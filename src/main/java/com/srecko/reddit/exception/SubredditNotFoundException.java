package com.srecko.reddit.exception;

public class SubredditNotFoundException extends RuntimeException {

    public SubredditNotFoundException(Long id) {
        super("Subreddit with id " + id + " is not found.");
    }
}
