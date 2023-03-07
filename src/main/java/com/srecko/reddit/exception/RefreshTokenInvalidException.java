package com.srecko.reddit.exception;

public class RefreshTokenInvalidException extends RuntimeException {

    public RefreshTokenInvalidException() {
        super("Refresh token is invalid. Log in again.");
    }
}
