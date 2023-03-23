package com.srecko.reddit.exception;

public class EmailVerificationTokenNotFoundException extends RuntimeException {

  public EmailVerificationTokenNotFoundException() {
    super("Verification token is not found.");
  }
}
