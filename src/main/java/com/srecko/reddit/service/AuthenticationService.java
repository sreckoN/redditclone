package com.srecko.reddit.service;

import com.srecko.reddit.dto.AuthenticationRequest;
import com.srecko.reddit.dto.AuthenticationResponse;
import com.srecko.reddit.dto.RegistrationRequest;
import com.srecko.reddit.entity.EmailVerificationToken;
import com.srecko.reddit.entity.User;

/**
 * The interface Authentication service.
 *
 * @author Srecko Nikolic
 */
public interface AuthenticationService {

  /**
   * Register.
   *
   * @param request         the request
   * @param confirmationUrl the confirmation url
   */
  void register(RegistrationRequest request, String confirmationUrl);

  /**
   * Save email verification token.
   *
   * @param token the token
   */
  void saveEmailVerificationToken(EmailVerificationToken token);

  /**
   * Authenticate authentication response.
   *
   * @param request the request
   * @return the authentication response
   */
  AuthenticationResponse authenticate(AuthenticationRequest request);

  /**
   * Generate email verification token.
   *
   * @param user   the user
   * @param appUrl the app url
   */
  void generateEmailVerificationToken(User user, String appUrl);

  /**
   * Gets verification token.
   *
   * @param token the token
   * @return the verification token
   */
  EmailVerificationToken getVerificationToken(String token);

  /**
   * Enable user account.
   *
   * @param user the user
   */
  void enableUserAccount(User user);
}
