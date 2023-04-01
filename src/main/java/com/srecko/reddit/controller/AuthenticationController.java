package com.srecko.reddit.controller;

import com.srecko.reddit.dto.AuthenticationRequest;
import com.srecko.reddit.dto.AuthenticationResponse;
import com.srecko.reddit.dto.RegistrationRequest;
import com.srecko.reddit.dto.TokenRefreshRequest;
import com.srecko.reddit.entity.EmailVerificationToken;
import com.srecko.reddit.exception.authentication.AuthenticationRequestInvalidException;
import com.srecko.reddit.exception.authentication.RegistrationRequestInvalidException;
import com.srecko.reddit.exception.authentication.TokenRefreshRequestInvalidException;
import com.srecko.reddit.service.AuthenticationService;
import com.srecko.reddit.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Authentication controller.
 *
 * @author Srecko Nikolic
 */
@RestController
@RequestMapping("/api/auth/")
public class AuthenticationController {

  private final AuthenticationService authenticationService;
  private final RefreshTokenService refreshTokenService;

  private static final Logger logger = LogManager.getLogger(AuthenticationController.class);

  /**
   * Instantiates a new Authentication controller.
   *
   * @param authenticationService the authentication service
   * @param refreshTokenService   the refresh token service
   */
  @Autowired
  public AuthenticationController(AuthenticationService authenticationService,
      RefreshTokenService refreshTokenService) {
    this.authenticationService = authenticationService;
    this.refreshTokenService = refreshTokenService;
  }

  /**
   * Register new user.
   *
   * @param request             the request
   * @param registrationRequest the registration request
   * @param bindingResult       the binding result
   * @return the response entity
   */
  @PostMapping("register")
  public ResponseEntity<String> register(HttpServletRequest request,
      @Valid @RequestBody RegistrationRequest registrationRequest, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new RegistrationRequestInvalidException(bindingResult.getAllErrors());
    }
    String confirmationUrl =
        request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + "/api/auth/registrationConfirm?token=";
    authenticationService.register(registrationRequest, confirmationUrl);
    logger.info("User registered successfully: {}", registrationRequest.getEmail());
    return ResponseEntity.ok(
        "User has been registered successfully. Verify your email to enable the account.");
  }

  /**
   * Confirm registration.
   *
   * @param token the token
   * @return the response entity
   */
  @GetMapping("registrationConfirm")
  public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token) {
    EmailVerificationToken verificationToken = authenticationService.getVerificationToken(token);
    authenticationService.enableUserAccount(verificationToken.getUser());
    logger.info("User's email successfully confirmed: {}", verificationToken.getUser().getEmail());
    return ResponseEntity.ok("Email confirmed! User account activated.");
  }

  /**
   * Authenticate user.
   *
   * @param authenticationRequest the authentication request
   * @param bindingResult         the binding result
   * @return the response entity
   */
  @PostMapping("authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @Valid @RequestBody AuthenticationRequest authenticationRequest,
      BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new AuthenticationRequestInvalidException(bindingResult.getAllErrors());
    }
    AuthenticationResponse authenticationResponse =
        authenticationService.authenticate(authenticationRequest);
    logger.info("User successfully authenticated: {}", authenticationResponse.getUsername());
    return ResponseEntity.ok(authenticationResponse);
  }

  /**
   * Refresh token.
   *
   * @param tokenRefreshRequest the token refresh request
   * @param bindingResult       the binding result
   * @return the response entity
   */
  @PostMapping("token/refresh")
  public ResponseEntity<AuthenticationResponse> refreshToken(
      @Valid @RequestBody TokenRefreshRequest tokenRefreshRequest, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new TokenRefreshRequestInvalidException(bindingResult.getAllErrors());
    }
    AuthenticationResponse newAccessToken = refreshTokenService.getNewAccessToken(
        tokenRefreshRequest);
    logger.info("New access token successfully generated for: {}", newAccessToken.getUsername());
    return ResponseEntity.ok(newAccessToken);
  }
}