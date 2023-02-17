package com.srecko.reddit.exception;

public class VerificationEmailSendingErrorException extends RuntimeException {

    public VerificationEmailSendingErrorException(String message) {
        super(message);
    }
}
