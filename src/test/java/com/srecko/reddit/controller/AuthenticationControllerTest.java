package com.srecko.reddit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srecko.reddit.dto.AuthenticationRequest;
import com.srecko.reddit.dto.AuthenticationResponse;
import com.srecko.reddit.dto.RegistrationRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Value("${sql.script.create.emailVerificationToken}")
    private String sqlAddEmailToken;

    @Value("${sql.script.create.user}")
    private String sqlAddUser;

    @Value("${sql.script.create.refreshToken}")
    private String sqlAddRefreshToken;

    @Value("${sql.script.delete.emailVerificationToken}")
    private String sqlDeleteEmailToken;

    @Value("${sql.script.delete.user}")
    private String sqlDeleteUser;

    @Value("${sql.script.delete.refreshToken}")
    private String sqlDeleteRefreshToken;

    private final String jwt = JwtTestUtils.getJwt();

    @BeforeEach
    void setUp() {
        jdbc.execute(sqlAddUser);
        jdbc.execute(sqlAddEmailToken);
        jdbc.execute(sqlAddRefreshToken);
    }

    @AfterEach
    void tearDown() {
        jdbc.execute(sqlDeleteEmailToken);
        jdbc.execute(sqlDeleteRefreshToken);
        jdbc.execute(sqlDeleteUser);
    }

    @Test
    void register() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest("John", "Doe", "srecko.nikolic71@hotmail.com", "johndoe", "password", "AU");
        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(registrationRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString)
                .servletPath("/api/auth/register"))
                .andExpect(status().isOk())
                .andExpect(content().string("User has been registered successfully. Verify your email to enable the account."));
    }

    @Test
    void registerThrowsEmailAlreadyInUseException() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest("John", "Doe", "janedoe@example.com", "johndoe", "password", "AU");
        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(registrationRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(valueAsString)
                        .servletPath("/api/auth/register"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Email janedoe@example.com is already in use")));
    }

    @Test
    void registerThrowsUsernameNotAvailableException() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest("John", "Doe", "johndoe@example.com", "janedoe", "password", "AU");
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
    void confirmRegistration() throws Exception {
        String token = "ea9b3023-f4b8-45f4-8e5a-664915c4e754";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/registrationConfirm")
                .servletPath("/api/auth/registrationConfirm")
                .param("token", token))
                .andExpect(status().isOk())
                .andExpect(content().string("Email confirmed! User account activated."));
    }

    @Test
    void authenticate() throws Exception {
        AuthenticationRequest authRequest = new AuthenticationRequest("janedoe", "iloveyou");
        ObjectMapper objectMapper = new ObjectMapper();
        String authRequestString = objectMapper.writeValueAsString(authRequest);

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(authenticationManager.authenticate(any(Authentication.class))).willReturn(authentication);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/authenticate")
                .servletPath("/api/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(authRequestString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is("janedoe")))
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    void refreshToken() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("janedoe", "iloveyou");
        String authRequestString = objectMapper.writeValueAsString(authenticationRequest);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authRequestString)
                        .servletPath("/api/auth/authenticate"))
                .andExpect(status().isOk())
                .andReturn();
        AuthenticationResponse authenticationResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), AuthenticationResponse.class);
        String refreshToken = authenticationResponse.getRefreshToken();
        String refreshTokenString = objectMapper.writeValueAsString(refreshToken);

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
    void refreshTokenThrowsRefreshTokenNotFoundException() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String refreshToken = JwtTestUtils.getJwt();
        String refreshTokenString = objectMapper.writeValueAsString(refreshToken);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/token/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshTokenString)
                        .servletPath("/api/auth/token/refresh"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Refresh token not found.")));
    }
}