package com.srecko.reddit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srecko.reddit.dto.CreatePostDto;
import com.srecko.reddit.dto.UpdatePostDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser(username = "janedoe", password = "iloveyou")
class PostControllerTest {

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

    @Value("${sql.script.create.post2}")
    private String sqlAddPostTwo;

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
        jdbc.execute(sqlAddPostTwo);
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
    void getAllPosts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getPost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/{postId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Todays News")))
                .andExpect(jsonPath("$.text", is("Nothing new")))
                .andExpect(jsonPath("$.user", is(2)))
                .andExpect(jsonPath("$.subreddit", is(1)));
    }

    @Test
    void getPostThrowsPostNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/{postId}", 0))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Post with id 0 is not found.")));
    }

    @Test
    void getAllPostsForSubreddit() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/subreddit/{subredditId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getAllPostsForSubredditThrowsSubredditNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/subreddit/{subredditId}", 0))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Subreddit with id 0 is not found.")));
    }

    @Test
    void getPostsForUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/user/{username}", "janedoe"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getPostsForUserThrowsUserNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/user/{username}", "janinedoe"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("User with username janinedoe is not found.")));
    }

    @Test
    @Disabled
    void create() throws Exception {
        CreatePostDto postDto = new CreatePostDto(1L, "I have a huge announcement", "Thats it");
        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(postDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(valueAsString))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("I have a huge announcement")))
                .andExpect(jsonPath("$.text", is("Thats it")))
                .andExpect(jsonPath("$.subreddit", is(1)))
                .andExpect(jsonPath("$.user", is(2)));
    }

    @Test
    @Disabled
    void createThrowsSubredditNotFoundException() throws Exception {
        CreatePostDto postDto = new CreatePostDto(0L, "I have a huge announcement", "Thats it");
        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(postDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(valueAsString))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Subreddit with id 0 is not found.")));
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/{postId}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Todays News")))
                .andExpect(jsonPath("$.text", is("Nothing new")))
                .andExpect(jsonPath("$.subreddit", is(1)))
                .andExpect(jsonPath("$.user", is(2)))
                .andExpect(jsonPath("$.votes", is(3)))
                .andExpect(jsonPath("$.commentsCounter", is(1)));
    }

    @Test
    void deleteThrowsPostNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/{postId}", 0))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Post with id 0 is not found.")));
    }

    @Test
    void update() throws Exception {
        UpdatePostDto postDto = new UpdatePostDto(1L, "Todays News", "Something new");
        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(postDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(valueAsString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Todays News")))
                .andExpect(jsonPath("$.text", is("Something new")))
                .andExpect(jsonPath("$.subreddit", is(1)))
                .andExpect(jsonPath("$.user", is(2)))
                .andExpect(jsonPath("$.votes", is(3)))
                .andExpect(jsonPath("$.commentsCounter", is(1)));
    }

    @Test
    void updateThrowsPostNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/{postId}", 0))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Post with id 0 is not found.")));
    }

    @Test
    void updateThrowsDtoValidationException() throws Exception {
        UpdatePostDto postDto = new UpdatePostDto(null, "Todays News", "Something new");
        ObjectMapper objectMapper = new ObjectMapper();
        String valueAsString = objectMapper.writeValueAsString(postDto);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(valueAsString))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("DTO validation failed for the following fields: postId.")));
    }
}