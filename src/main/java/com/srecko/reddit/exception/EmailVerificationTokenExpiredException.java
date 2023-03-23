package com.srecko.reddit.exception;

public class EmailVerificationTokenExpiredException extends RuntimeException {

  public EmailVerificationTokenExpiredException() {
    super("Email Verification token expired. Register again.");
  }
}