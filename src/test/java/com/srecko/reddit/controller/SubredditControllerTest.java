package com.srecko.reddit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srecko.reddit.dto.SubredditDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser(username = "janedoe", password = "iloveyou")
@WithUserDetails("janedoe")
class SubredditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbc;

    @Value("${sql.script.create.user}")
    private String sqlAddUser;

    @Value("${sql.script.create.subreddit}")
    private String sqlAddSubreddit;

    @Value("${sql.script.delete.user}")
    private String sqlDeleteUser;

    @Value("${sql.script.delete.subreddit}")
    private String sqlDeleteSubreddit;

    @BeforeEach
    void setUp() {
        jdbc.execute(sqlAddUser);
        jdbc.execute(sqlAddSubreddit);
    }

    @AfterEach
    void tearDown() {
        jdbc.execute(sqlDeleteSubreddit);
        jdbc.execute(sqlDeleteUser);
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/subreddits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getSubreddit() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/subreddits/{subredditId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Serbias subreddit")))
                .andExpect(jsonPath("$.description", is("Welcome to Serbia")))
                .andExpect(jsonPath("$.creator", is(2)));
    }

    @Test
    void getSubredditThrowsSubredditNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/subreddits/{subredditId}", 0))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Subreddit with id 0 is not found.")));
    }

    @Test
    @WithMockCustomUser
    void save() throws Exception {
        SubredditDto subredditDto = new SubredditDto(0L, "Greeces subreddit", "Official subreddit");
        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(subredditDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/subreddits")
                .contentType(APPLICATION_JSON)
                .content(valueAsString))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Greeces subreddit")))
                .andExpect(jsonPath("$.description", is("Official subreddit")));
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/subreddits/{subredditId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Serbias subreddit")))
                .andExpect(jsonPath("$.description", is("Welcome to Serbia")))
                .andExpect(jsonPath("$.creator", is(2)));
    }

    @Test
    void deleteThrowsSubredditNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/subreddits/{subredditId}", 0))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Subreddit with id 0 is not found.")));
    }

    @Test
    void update() throws Exception {
        SubredditDto subredditDto = new SubredditDto(1L, "Serbias subreddit", "Welcome to Yugoslavia");
        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(subredditDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/subreddits")
                        .contentType(APPLICATION_JSON)
                        .content(valueAsString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Serbias subreddit")))
                .andExpect(jsonPath("$.description", is("Welcome to Yugoslavia")));
    }

    @Test
    void updateThrowsSubredditNotFoundException() throws Exception {
        SubredditDto subredditDto = new SubredditDto(0L, "Serbias subreddit", "Welcome to Yugoslavia");
        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(subredditDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/subreddits")
                        .contentType(APPLICATION_JSON)
                        .content(valueAsString))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Subreddit with id 0 is not found.")));
    }

    @Test
    void updateThrowsDtoValidationException() throws Exception {
        SubredditDto subredditDto = new SubredditDto(null, "Serbias subreddit", "Welcome to Yugoslavia");
        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(subredditDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/subreddits")
                        .contentType(APPLICATION_JSON)
                        .content(valueAsString))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("DTO validation failed for the following fields: subredditId.")));
    }
}