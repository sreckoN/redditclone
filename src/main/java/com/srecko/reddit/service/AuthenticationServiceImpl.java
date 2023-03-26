package com.srecko.reddit.service;

import com.srecko.reddit.dto.AuthenticationRequest;
import com.srecko.reddit.dto.AuthenticationResponse;
import com.srecko.reddit.dto.RegistrationRequest;
import com.srecko.reddit.entity.EmailVerificationToken;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.authentication.EmailAlreadyInUseException;
import com.srecko.reddit.exception.authentication.EmailVerificationTokenExpiredException;
import com.srecko.reddit.exception.authentication.EmailVerificationTokenNotFoundException;
import com.srecko.reddit.exception.authentication.EmailVerificationTokenInvalidException;
import com.srecko.reddit.exception.authentication.RegistrationRequestNullException;
import com.srecko.reddit.exception.authentication.UsernameNotAvailableException;
import com.srecko.reddit.exception.authentication.VerificationEmailSendingErrorException;
import com.srecko.reddit.jwt.JwtUtils;
import com.srecko.reddit.repository.EmailVerificationRepository;
import jakarta.mail.MessagingException;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Authentication service.
 *
 * @author Srecko Nikolic
 */
@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final EmailVerificationRepository emailVerificationRepository;
  private final EmailService emailService;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final JwtUtils jwtUtils;
  private final RefreshTokenService refreshTokenService;

  /**
   * Instantiates a new Authentication service.
   *
   * @param userService                  the user service
   * @param passwordEncoder              the password encoder
   * @param emailVerificationRepository  the email verification repository
   * @param emailService                 the email service
   * @param authenticationManagerBuilder the authentication manager builder
   * @param jwtUtils                     the jwt utils
   * @param refreshTokenService          the refresh token service
   */
  @Autowired
  public AuthenticationServiceImpl(UserService userService, PasswordEncoder passwordEncoder,
      EmailVerificationRepository emailVerificationRepository, EmailService emailService,
      AuthenticationManagerBuilder authenticationManagerBuilder, JwtUtils jwtUtils,
      RefreshTokenService refreshTokenService) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.emailVerificationRepository = emailVerificationRepository;
    this.emailService = emailService;
    this.authenticationManagerBuilder = authenticationManagerBuilder;
    this.jwtUtils = jwtUtils;
    this.refreshTokenService = refreshTokenService;
  }

  @Override
  public void register(RegistrationRequest request, String confirmationUrl) {
    if (request == null) {
      throw new RegistrationRequestNullException();
    } else if (userService.existsUserByEmail(request.getEmail())) {
      throw new EmailAlreadyInUseException(request.getEmail());
    } else if (userService.existsUserByUsername(request.getUsername())) {
      throw new UsernameNotAvailableException(request.getUsername());
    } else {
      User user = new User(request.getFirstName(),
          request.getLastName(),
          request.getEmail(),
          request.getUsername(),
          passwordEncoder.encode(request.getPassword()),
          request.getCountry(),
          false);
      userService.save(user);
      sendVerificationEmail(user, confirmationUrl);
    }
  }

  @Override
  public void sendVerificationEmail(User user, String appUrl) {
    EmailVerificationToken token = new EmailVerificationToken(user);
    emailVerificationRepository.save(token);
    String recipientEmail = user.getEmail();
    String confirmationUrl = appUrl + token.getToken();
    Map<String, Object> vars = new HashMap<>();
    vars.put("recipientName", user.getFirstName());
    vars.put("confirmationUrl", confirmationUrl);
    try {
      emailService.sendMessageUsingThymeleafTemplate(recipientEmail, "Verify your email address",
          vars);
    } catch (MessagingException e) {
      throw new VerificationEmailSendingErrorException(e.getMessage());
    }
  }

  @Override
  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
        request.getUsername(), request.getPassword());
    authenticationManagerBuilder.getOrBuild().authenticate(token);
    User user = userService.getUserByUsername(request.getUsername());
    String accessToken = jwtUtils.getAccessToken(user.getEmail());
    String refreshToken = jwtUtils.getRefreshToken(user.getEmail());
    refreshTokenService.saveRefreshToken(refreshToken, user);
    return new AuthenticationResponse(user.getUsername(), accessToken, refreshToken);
  }

  @Override
  public EmailVerificationToken getVerificationToken(String token) {
    Optional<EmailVerificationToken> emailVerificationTokenOptional =
        emailVerificationRepository.getEmailVerificationTokenByToken(token);
    if (emailVerificationTokenOptional.isEmpty()) {
      throw new EmailVerificationTokenNotFoundException();
    }
    EmailVerificationToken verificationToken = emailVerificationTokenOptional.get();
    if (!verificationToken.getToken().equals(token)) {
      throw new EmailVerificationTokenInvalidException();
    }
    if (isVerificationTokenExpired(verificationToken)) {
      throw new EmailVerificationTokenExpiredException();
    }
    return verificationToken;
  }

  @Override
  public void enableUserAccount(User user) {
    user.setEnabled(true);
    userService.save(user);
    emailVerificationRepository.deleteByUser_Id(user.getId());
  }

  private boolean isVerificationTokenExpired(EmailVerificationToken token) {
    Instant currentTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
    return token.getExpiryDate().before(Date.from(currentTime));
  }
}