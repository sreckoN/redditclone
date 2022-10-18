package com.srecko.reddit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srecko.reddit.dto.CommentDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser(username = "janedoe", password = "iloveyou")
@WithUserDetails("janedoe")
@Transactional
class CommentControllerTest {

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

    @Value("${sql.script.delete.user}")
    private String sqlDeleteUser;

    @Value("${sql.script.delete.subreddit}")
    private String sqlDeleteSubreddit;

    @Value("${sql.script.delete.post}")
    private String sqlDeletePost;

    @Value("${sql.script.delete.comment}")
    private String sqlDeleteComment;

    @BeforeEach
    void setUp() {
        jdbc.execute(sqlAddUser);
        jdbc.execute(sqlAddSubreddit);
        jdbc.execute(sqlAddPost);
        jdbc.execute(sqlAddComment);
    }

    @AfterEach
    void tearDown() {
        jdbc.execute(sqlDeleteComment);
        jdbc.execute(sqlDeletePost);
        jdbc.execute(sqlDeleteSubreddit);
        jdbc.execute(sqlDeleteUser);
    }

    @Test
    void getCommentsForPost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/comments/post/{postId}", 2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].text", is("Oh well")))
                .andExpect(jsonPath("$[0].post", is(2)))
                .andExpect(jsonPath("$[0].user", is(2)))
                .andExpect(jsonPath("$[0].votes", is(5)));
    }

    @Test
    void getCommentsForPostThrowsPostNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/comments/post/{postId}", 0))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Post with id 0 is not found.")));
    }

    @Test
    void getCommentsForUsername() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/comments/user/{username}", "janedoe"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].text", is("Oh well")))
                .andExpect(jsonPath("$[0].votes", is(5)))
                .andExpect(jsonPath("$[0].post", is(2)));
    }

    @Test
    void getCommentsForUsernameThrowsUserNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/comments/user/{username}", "jakedoe"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("User with username jakedoe is not found.")));
    }

    @Test
    @WithMockCustomUser
    void createComment() throws Exception {
        CommentDto commentDto = new CommentDto("New comment", 2L);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(commentDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.text", is("New comment")))
                .andExpect(jsonPath("$.votes", is(0)))
                .andExpect(jsonPath("$.post", is(2)))
                .andExpect(jsonPath("$.user", is(2)));
    }

    @Test
    @WithMockCustomUser
    void createCommentThrowsPostNotFoundException() throws Exception {
        CommentDto commentDto = new CommentDto("New comment", 0L);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(commentDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Post with id 0 is not found.")));
    }

    @Test
    void deleteComment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/comments/{commentId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.text", is("Oh well")))
                .andExpect(jsonPath("$.votes", is(5)))
                .andExpect(jsonPath("$.post", is(2)));
    }

    @Test
    void deleteCommentThrowsCommentNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/comments/{commentId}", 0))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Comment with id 0 is not found")));
    }
}