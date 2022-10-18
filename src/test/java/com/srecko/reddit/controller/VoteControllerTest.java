package com.srecko.reddit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srecko.reddit.dto.VoteDto;
import com.srecko.reddit.entity.VoteType;
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

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser(username = "janedoe", password = "iloveyou")
@WithUserDetails("janedoe")
class VoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbc;

    @Value("${sql.script.create.user}")
    private String sqlAddUser;

    @Value("${sql.script.create.subreddit}")
    private String sqlAddSubreddit;

    @Value("${sql.script.create.post}")
    private String sqlAddPost;

    @Value("${sql.script.create.comment}")
    private String sqlAddComment;

    @Value("${sql.script.create.vote}")
    private String sqlAddVote;

    @Value("${sql.script.delete.user}")
    private String sqlDeleteUser;

    @Value("${sql.script.delete.subreddit}")
    private String sqlDeleteSubreddit;

    @Value("${sql.script.delete.post}")
    private String sqlDeletePost;

    @Value("${sql.script.delete.comment}")
    private String sqlDeleteComment;

    @Value("${sql.script.delete.vote}")
    private String sqlDeleteVote;

    @BeforeEach
    void setUp() {
        jdbc.execute(sqlAddUser);
        jdbc.execute(sqlAddSubreddit);
        jdbc.execute(sqlAddPost);
        jdbc.execute(sqlAddComment);
        jdbc.execute(sqlAddVote);
    }

    @AfterEach
    void tearDown() {
        jdbc.execute(sqlDeleteVote);
        jdbc.execute(sqlDeleteComment);
        jdbc.execute(sqlDeletePost);
        jdbc.execute(sqlDeleteSubreddit);
        jdbc.execute(sqlDeleteUser);
    }

    @Test
    @WithMockCustomUser
    void save() throws Exception {
        VoteDto voteDto = new VoteDto(2L, VoteType.UPVOTE);
        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(voteDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/votes")
                .contentType(APPLICATION_JSON)
                .content(valueAsString))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.post", is(2)))
                .andExpect(jsonPath("$.user", is(2)));
    }

    @Test
    @WithMockCustomUser
    void saveThrowsPostNotFoundException() throws Exception {
        VoteDto voteDto = new VoteDto(0L, VoteType.UPVOTE);
        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(voteDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/votes")
                        .contentType(APPLICATION_JSON)
                        .content(valueAsString))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Post with id 0 is not found.")));
    }

    @Test
    void saveThrowsDtoValidationException() throws Exception {
        VoteDto voteDto = new VoteDto(null, VoteType.UPVOTE);
        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(voteDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/votes")
                        .contentType(APPLICATION_JSON)
                        .content(valueAsString))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("DTO validation failed for the following fields: postId.")));
    }

    @Test
    @WithMockCustomUser
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/votes/{voteId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    @WithMockCustomUser
    void deleteThrowsVoteNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/votes/{voteId}", 0))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Vote with id 0 is not found.")));
    }
}