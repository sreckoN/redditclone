package com.srecko.reddit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.srecko.reddit.dto.AuthenticationRequest;
import com.srecko.reddit.dto.AuthenticationResponse;
import com.srecko.reddit.dto.RegistrationRequest;
import com.srecko.reddit.entity.EmailVerificationToken;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.authentication.EmailAlreadyInUseException;
import com.srecko.reddit.exception.authentication.EmailVerificationTokenExpiredException;
import com.srecko.reddit.exception.authentication.EmailVerificationTokenInvalidException;
import com.srecko.reddit.exception.authentication.EmailVerificationTokenNotFoundException;
import com.srecko.reddit.exception.authentication.RegistrationRequestNullException;
import com.srecko.reddit.exception.authentication.UsernameNotAvailableException;
import com.srecko.reddit.jwt.JwtUtils;
import com.srecko.reddit.repository.EmailVerificationRepository;
import jakarta.mail.MessagingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AuthenticationServiceImpl.class})
@ExtendWith(SpringExtension.class)
class AuthenticationServiceImplTest {

  @Autowired
  private AuthenticationServiceImpl authenticationServiceImpl;

  @MockBean
  private PasswordEncoder passwordEncoder;

  @MockBean
  private UserService userService;

  @MockBean
  private EmailVerificationRepository emailVerificationRepository;

  @MockBean
  private EmailService emailService;

  @MockBean
  private AuthenticationManagerBuilder authenticationManagerBuilder;

  @MockBean
  private JwtUtils jwtUtils;

  @MockBean
  private RefreshTokenService refreshTokenService;

  private User user;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setComments(new ArrayList<>());
    user.setCountry("GB");
    user.setEmail("jane.doe@example.org");
    user.setEnabled(true);
    user.setFirstName("Jane");
    user.setId(123L);
    user.setLastName("Doe");
    user.setPassword("iloveyou");
    user.setPosts(new ArrayList<>());
    LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
    user.setRegistrationDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
    user.setUsername("janedoe");
  }

  @Test
  void register_ThrowsEmailAlreadyInUse_WhenGivenAlreadyUsedEmail() {
    // given
    given(userService.existsUserByEmail(any())).willReturn(true);

    // when then
    assertThrows(EmailAlreadyInUseException.class, () -> authenticationServiceImpl
        .register(
            new RegistrationRequest("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou",
                "GB"), ""));
  }

  @Test
  void register_ThrowsUsernameNotAvailableException_WhenGivenUnavailableUsername() {
    // given
    given(userService.existsUserByEmail(any())).willReturn(false);
    given(userService.existsUserByUsername(any())).willReturn(true);

    // when
    assertThrows(UsernameNotAvailableException.class, () -> authenticationServiceImpl
        .register(
            new RegistrationRequest("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou",
                "GB"), ""));
  }

  @Test
  void register_ThrowsRegistrationRequestNullException_WhenGivenNullRegistrationRequest() {
    // given
    when(userService.existsUserByEmail(any())).thenReturn(true);
    when(userService.existsUserByUsername(any())).thenReturn(true);
    when(userService.save(any())).thenReturn(user);

    // when then
    assertThrows(RegistrationRequestNullException.class, () -> {
      authenticationServiceImpl.register(null, "");
    });
  }

  @Test
  void register_ReturnsStringMessage_WhenUserSuccessfullyRegistered() {
    // given
    given(userService.existsUserByEmail(any())).willReturn(false);
    given(userService.existsUserByUsername(any())).willReturn(false);

    // when
    authenticationServiceImpl.register(
        new RegistrationRequest(user.getFirstName(), user.getLastName(),
            user.getEmail(), user.getUsername(), user.getPassword(), user.getCountry()), "");

    // then
    verify(userService).save(any());
  }

  @Test
  void sendVerificationEmail() throws MessagingException {
    // given
    // when
    authenticationServiceImpl.sendVerificationEmail(user,
        "http://localhost:8080/api/auth");
    // then
    verify(emailService).sendMessageUsingThymeleafTemplate(any(String.class), any(String.class), any(Map.class));
  }

  @Test
  void getVerificationToken_ReturnsEmailVerificationToken_WhenSuccessfullyCreated() throws ParseException {
    // given
    EmailVerificationToken token = new EmailVerificationToken();
    token.setToken("ea9b3023-f4b8-45f4-8e5a-664915c4e888");
    String dateString = "2025-02-15";
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    token.setExpiryDate(format.parse(dateString));
    given(
        emailVerificationRepository.getEmailVerificationTokenByToken(token.getToken())).willReturn(
        Optional.of(token));

    // when
    EmailVerificationToken returnedToken = authenticationServiceImpl.getVerificationToken(
        token.getToken());

    // then
    assertEquals(token.getToken(), returnedToken.getToken());
  }

  @Test
  void getVerificationToken_ThrowsEmailVerificationTokenNotFoundException_WhenTokenDoesNotExist() throws ParseException {
    // given
    EmailVerificationToken token = new EmailVerificationToken();
    token.setToken("ea9b3023-f4b8-45f4-8e5a-664915c4e888");
    given(
        emailVerificationRepository.getEmailVerificationTokenByToken(token.getToken())).willReturn(
        Optional.empty());

    // when then
    assertThrows(EmailVerificationTokenNotFoundException.class, () -> {
      authenticationServiceImpl.getVerificationToken(token.getToken());
    });
  }

  @Test
  void getVerificationToken_ThrowsInvalidEmailVerificationTokenException_WhenInvalidTokenGiven() throws ParseException {
    // given
    EmailVerificationToken token = new EmailVerificationToken();
    token.setToken("ea9b3023-f4b8-45f4-8e5a-664915c4e888");
    given(emailVerificationRepository.getEmailVerificationTokenByToken(any())).willReturn(
        Optional.of(token));

    // when then
    assertThrows(EmailVerificationTokenInvalidException.class, () -> {
      authenticationServiceImpl.getVerificationToken("ea9b3023-f4b8-45f4-8e5a-4e888");
    });
  }

  @Test
  void getVerificationToken_ThrowsEmailVerificationTokenExpiredException_WhenTokenExpired() throws ParseException {
    // given
    EmailVerificationToken token = new EmailVerificationToken();
    token.setToken("ea9b3023-f4b8-45f4-8e5a-664915c4e888");
    String dateString = "1990-02-15";
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    token.setExpiryDate(format.parse(dateString));
    given(
        emailVerificationRepository.getEmailVerificationTokenByToken(token.getToken())).willReturn(
        Optional.of(token));

    // when then
    assertThrows(EmailVerificationTokenExpiredException.class, () -> {
      authenticationServiceImpl.getVerificationToken(token.getToken());
    });
  }

  @Test
  void enableUserAccount() {
    //given
    given(userService.save(any())).willReturn(null);

    // when
    authenticationServiceImpl.enableUserAccount(user);

    // then
    verify(userService).save(any(User.class));
  }

  @Test
  void authenticate_ReturnsAuthenticationResponse_WhenUserSuccessfullyAuthenticated() {
    // given
    String accessToken = "testAccessToken";
    String refreshToken = "testRefreshToken";
    User user = new User("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou", "GB", true);
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
    AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    given(authenticationManagerBuilder.getOrBuild()).willReturn(authenticationManager);
    given(authenticationManager.authenticate(token)).willReturn(null);
    given(userService.getUserByUsername(user.getUsername())).willReturn(user);
    given(jwtUtils.getAccessToken(any())).willReturn(accessToken);
    given(jwtUtils.getRefreshToken(any())).willReturn(refreshToken);

    // when
    AuthenticationRequest authenticationRequest = new AuthenticationRequest(user.getUsername(), user.getPassword());
    AuthenticationResponse authenticationResponse = authenticationServiceImpl.authenticate(authenticationRequest);

    // then
    assertEquals(user.getUsername(), authenticationResponse.getUsername());
    assertEquals(accessToken, authenticationResponse.getAccessToken());
    assertEquals(refreshToken, authenticationResponse.getRefreshToken());
  }
}