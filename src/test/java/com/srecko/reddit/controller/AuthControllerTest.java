package com.srecko.reddit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srecko.reddit.dto.RegistrationRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbc;

    @Value("${sql.script.create.user}")
    private String sqlAddUser;

    @Value("${sql.script.delete.user}")
    private String sqlDeleteUser;

    @BeforeEach
    void setUp() {
        jdbc.execute(sqlAddUser);
    }

    @AfterEach
    void tearDown() {
        jdbc.execute(sqlDeleteUser);
    }

    @Test
    void signup() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest("John", "Doe", "johndoe@example.com", "johndoe", "password", "AU");
        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(registrationRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString))
                .andExpect(status().isOk())
                .andExpect(content().string("User has been registered successfully."));
    }

    @Test
    void signupThrowsEmailAlreadyInUseException() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest("John", "Doe", "janedoe@example.com", "johndoe", "password", "AU");
        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(registrationRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(valueAsString))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Email janedoe@example.com is already in use")));
    }

    @Test
    void signupThrowsUsernameNotAvailableException() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest("John", "Doe", "johndoe@example.com", "janedoe", "password", "AU");
        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(registrationRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(valueAsString))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Username janedoe is already in use.")));
    }

    @Test
    void refreshTokenThrowsRuntimeException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/token/refresh"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Authorization header is missing.")));
    }
}