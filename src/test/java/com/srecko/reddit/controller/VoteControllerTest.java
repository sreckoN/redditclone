package com.srecko.reddit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srecko.reddit.dto.VoteCommentDto;
import com.srecko.reddit.dto.VoteDto;
import com.srecko.reddit.dto.VotePostDto;
import com.srecko.reddit.entity.VoteType;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

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

    @Value("${sql.script.create.postVote}")
    private String sqlAddPostVote;

    @Value("${sql.script.create.commentVote}")
    private String sqlAddCommentVote;

    @Value("${sql.script.delete.user}")
    private String sqlDeleteUser;

    @Value("${sql.script.delete.subreddit}")
    private String sqlDeleteSubreddit;

    @Value("${sql.script.delete.post}")
    private String sqlDeletePost;

    @Value("${sql.script.delete.comment}")
    private String sqlDeleteComment;

    @Value("${sql.script.delete.votes}")
    private String sqlDeleteVotes;

    private final String jwt = JwtTestUtils.getJwt();

    @BeforeEach
    void setUp() {
        jdbc.execute(sqlAddUser);
        jdbc.execute(sqlAddSubreddit);
        jdbc.execute(sqlAddPost);
        jdbc.execute(sqlAddComment);
        jdbc.execute(sqlAddPostVote);
        jdbc.execute(sqlAddCommentVote);
    }

    @AfterEach
    void tearDown() {
        jdbc.execute(sqlDeleteVotes);
        jdbc.execute(sqlDeleteComment);
        jdbc.execute(sqlDeletePost);
        jdbc.execute(sqlDeleteSubreddit);
        jdbc.execute(sqlDeleteUser);
    }

    @Test
    @WithMockCustomUser
    void savePostVote() throws Exception {
        VoteDto voteDto = new VotePostDto(VoteType.UPVOTE, 2L);
        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(voteDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/votes/post")
                    .contentType(APPLICATION_JSON)
                    .content(valueAsString)
                    .header("AUTHORIZATION", "Bearer " + jwt))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.post", is(2)))
                .andExpect(jsonPath("$.user", is(2)))
                .andExpect(jsonPath("$.type", is("UPVOTE")));
    }

    @Test
    @WithMockCustomUser
    void savePostVoteThrowsPostNotFoundException() throws Exception {
        VoteDto voteDto = new VotePostDto(VoteType.UPVOTE, 0L);
        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(voteDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/votes/post")
                        .contentType(APPLICATION_JSON)
                        .content(valueAsString)
                        .header("AUTHORIZATION", "Bearer " + jwt))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Post with id 0 is not found.")));
    }

    @Test
    void savePostVoteThrowsDtoValidationException() throws Exception {
        VoteDto voteDto = new VotePostDto(VoteType.UPVOTE, null);
        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(voteDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/votes/post")
                        .contentType(APPLICATION_JSON)
                        .content(valueAsString)
                        .header("AUTHORIZATION", "Bearer " + jwt))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("DTO validation failed for the following fields: postId.")));
    }

    @Test
    @WithMockCustomUser
    void saveCommentVote() throws Exception {
        VoteDto voteDto = new VoteCommentDto(VoteType.UPVOTE, 1L);
        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(voteDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/votes/comment")
                        .contentType(APPLICATION_JSON)
                        .content(valueAsString)
                        .header("AUTHORIZATION", "Bearer " + jwt))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.comment", is(1)))
                .andExpect(jsonPath("$.user", is(2)))
                .andExpect(jsonPath("$.type", is("UPVOTE")));
    }

    @Test
    @WithMockCustomUser
    void saveCommentVoteThrowsCommentNotFoundException() throws Exception {
        VoteDto voteDto = new VoteCommentDto(VoteType.UPVOTE, 0L);
        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(voteDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/votes/comment")
                        .contentType(APPLICATION_JSON)
                        .content(valueAsString)
                        .header("AUTHORIZATION", "Bearer " + jwt))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Comment with id 0 is not found.")));
    }

    @Test
    void saveCommentVoteThrowsDtoValidationException() throws Exception {
        VoteDto voteDto = new VoteCommentDto(VoteType.UPVOTE, null);
        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(voteDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/votes/comment")
                        .contentType(APPLICATION_JSON)
                        .content(valueAsString)
                        .header("AUTHORIZATION", "Bearer " + jwt))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("DTO validation failed for the following fields: commentId.")));
    }

    @Test
    @WithMockCustomUser
    void deletePostVote() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/votes/post/{voteId}", 3)
                        .header("AUTHORIZATION", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)));
    }

    @Test
    @WithMockCustomUser
    void deletePostVoteThrowsVoteNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/votes/post/{voteId}", 0)
                        .header("AUTHORIZATION", "Bearer " + jwt))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Vote with id 0 is not found.")));
    }

    @Test
    @WithMockCustomUser
    void deleteCommentVote() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/votes/comment/{voteId}", 4)
                        .header("AUTHORIZATION", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(4)));
    }

    @Test
    @WithMockCustomUser
    void deleteCommentVoteThrowsVoteNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/votes/comment/{voteId}", 0)
                        .header("AUTHORIZATION", "Bearer " + jwt))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Vote with id 0 is not found.")));
    }
}