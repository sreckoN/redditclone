package com.srecko.reddit.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srecko.reddit.controller.utils.JwtTestUtils;
import com.srecko.reddit.dto.requests.AuthenticationRequest;
import com.srecko.reddit.dto.requests.RegistrationRequest;
import com.srecko.reddit.dto.requests.TokenRefreshRequest;
import com.srecko.reddit.dto.responses.AuthenticationResponse;
import com.srecko.reddit.entity.EmailVerificationToken;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.repository.EmailVerificationRepository;
import com.srecko.reddit.repository.RefreshTokenRepository;
import com.srecko.reddit.repository.UserRepository;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class AuthenticationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EmailVerificationRepository emailVerificationRepository;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private User user;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    user = new User("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou", "GB", true);
  }

  @AfterEach
  void tearDown() {
    emailVerificationRepository.deleteAll();
    refreshTokenRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void register_ReturnsStringJson_WhenUSerSuccessfullyRegistered() throws Exception {
    RegistrationRequest registrationRequest = new RegistrationRequest("John", "Doe",
        "srecko.nikolic71@hotmail.com", "johndoe", "password", "AU");
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(registrationRequest);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString)
            .servletPath("/api/auth/register"))
        .andExpect(status().isOk())
        .andExpect(content().string(
            "User has been registered successfully. Verify your email to enable the account."));
  }

  @Test
  void register_ThrowsEmailAlreadyInUseException_WhenGivenEmailIsAlreadyRegistered() throws Exception {
    userRepository.save(user);
    RegistrationRequest registrationRequest = new RegistrationRequest("John", "Doe",
        user.getEmail(), "johndoe", "password", "AU");
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(registrationRequest);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString)
            .servletPath("/api/auth/register"))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Email " + user.getEmail() + " is already in use")));
  }

  @Test
  void register_ThrowsUsernameNotAvailableException_WhenGivenUsernameAlreadyInUse() throws Exception {
    userRepository.save(user);
    RegistrationRequest registrationRequest = new RegistrationRequest("John", "Doe",
        "johndoe@example.com", "janedoe", "password", "AU");
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(registrationRequest);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString)
            .servletPath("/api/auth/register"))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Username janedoe is already in use.")));
  }

  @Test
  void register_RegistrationRequestException_WhenInvalidRegistrationRequest() throws Exception {
    RegistrationRequest registrationRequest = new RegistrationRequest();
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(registrationRequest);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString)
            .servletPath("/api/auth/register"))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value(containsString("firstName")))
        .andExpect(jsonPath("$.message").value(containsString("email")))
        .andExpect(jsonPath("$.message").value(containsString("password")))
        .andExpect(jsonPath("$.message").value(containsString("username")));
  }

  @Test
  void confirmRegistration_ReturnsJsonString_WhenRegistrationConfirmed() throws Exception {
    userRepository.save(user);
    String token = "ea9b3023-f4b8-45f4-8e5a-664915c4e754";
    EmailVerificationToken emailToken = new EmailVerificationToken(user);
    emailToken.setToken(token);
    LocalDate currentDatePlusOneDay = LocalDate.now().plusDays(1);
    emailToken.setExpiryDate(Date.from(currentDatePlusOneDay.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    emailVerificationRepository.save(emailToken);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/registrationConfirm")
            .servletPath("/api/auth/registrationConfirm")
            .param("token", token))
        .andExpect(status().isOk())
        .andExpect(content().string("Email confirmed! User account activated."));
  }

  @Test
  void authenticate_ReturnsAuthenticationResponse_WhenSuccessfullyAuthenticated() throws Exception {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);
    AuthenticationRequest authRequest = new AuthenticationRequest(user.getUsername(), "iloveyou");
    ObjectMapper objectMapper = new ObjectMapper();
    String authRequestString = objectMapper.writeValueAsString(authRequest);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/authenticate")
            .servletPath("/api/auth/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .content(authRequestString))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.accessToken").isNotEmpty())
        .andExpect(jsonPath("$.refreshToken").isNotEmpty());
  }

  @Test
  void refreshToken() throws Exception {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);
    ObjectMapper objectMapper = new ObjectMapper();
    AuthenticationRequest authenticationRequest = new AuthenticationRequest("janedoe", "iloveyou");
    String authRequestString = objectMapper.writeValueAsString(authenticationRequest);

    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .content(authRequestString)
            .servletPath("/api/auth/authenticate"))
        .andExpect(status().isOk())
        .andReturn();

    AuthenticationResponse authenticationResponse = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(), AuthenticationResponse.class);
    TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest(authenticationResponse.getRefreshToken());
    String refreshTokenString = objectMapper.writeValueAsString(tokenRefreshRequest);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/token/refresh")
            .contentType(MediaType.APPLICATION_JSON)
            .content(refreshTokenString)
            .servletPath("/api/auth/token/refresh"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.username", is("janedoe")))
        .andExpect(jsonPath("$.accessToken").isNotEmpty())
        .andExpect(jsonPath("$.refreshToken").isNotEmpty());
  }

  @Test
  void refreshToken_ThrowsRefreshTokenNotFoundException_WhenTokenNotFound() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest(JwtTestUtils.getJwt());
    String refreshTokenString = objectMapper.writeValueAsString(tokenRefreshRequest);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/token/refresh")
            .contentType(MediaType.APPLICATION_JSON)
            .content(refreshTokenString)
            .servletPath("/api/auth/token/refresh"))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.message", is("Refresh token not found.")));
  }

  @Test
  void refreshToken_ThrowsTokenRefreshRequestInvalidException_WhenInvalidTokenRefreshRequest() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest();
    String refreshTokenRequestString = objectMapper.writeValueAsString(tokenRefreshRequest);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/token/refresh")
            .contentType(MediaType.APPLICATION_JSON)
            .content(refreshTokenRequestString)
            .servletPath("/api/auth/token/refresh"))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.message", is("Token refresh request validation failed for the following fields: refreshToken.")));
  }
}