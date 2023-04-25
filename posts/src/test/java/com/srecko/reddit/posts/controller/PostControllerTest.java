package com.srecko.reddit.posts.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srecko.reddit.posts.dto.CreatePostRequest;
import com.srecko.reddit.posts.dto.UpdatePostRequest;
import com.srecko.reddit.posts.entity.Post;
import com.srecko.reddit.posts.repository.PostRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class PostControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private PostRepository postRepository;

//  private final String jwt = JwtTestUtils.getJwt();
  private final String jwt = "liul29j93iffj.2t34tg2.2g4gra";

  private Long userId;

  private Long subredditId;

  @BeforeEach
  void setUp() {
    postRepository.deleteAll();
    userId = 123L;
    subredditId = 234L;
  }

  @AfterEach
  void tearDown() {
    postRepository.deleteAll();
  }

  @Test
  void getAllPosts_ReturnsPosts() throws Exception {
    Post post1 = new Post(userId, "I love you.", "I do.", subredditId);
    Post post2 = new Post(userId, "What's up.", "Not much.", subredditId);
    postRepository.saveAll(List.of(post1, post2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/posts")
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].postDtoList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.postDtoList[0].title", is(post1.getTitle())))
        .andExpect(jsonPath("$._embedded.postDtoList[1].title", is(post2.getTitle())))
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void getPost_ReturnsPost_WhenPostExists() throws Exception {
    Post post = new Post(userId, "I love you.", "I do.", subredditId);
    postRepository.save(post);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/{postId}", post.getId())
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.id", is(post.getId().intValue())))
        .andExpect(jsonPath("$.title", is(post.getTitle())))
        .andExpect(jsonPath("$.text", is(post.getText())))
        .andExpect(jsonPath("$.user", is(post.getUserId().intValue())))
        .andExpect(jsonPath("$.subreddit", is(post.getSubredditId().intValue())));
  }

  @Test
  void getPost_ThrowsPostNotFoundException_WhenPostDoesNotExist() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/{postId}", 0)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Post with id 0 is not found.")));
  }

  @Test
  void getAllPostsForSubreddit_ReturnsPosts_WhenSubredditExists() throws Exception {
    Post post1 = new Post(userId, "I love you.", "I do.", subredditId);
    Post post2 = new Post(userId, "What's up.", "Not much.", subredditId);
    postRepository.saveAll(List.of(post1, post2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/subreddit/{subredditId}", subredditId)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].postDtoList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.postDtoList[0].title", is(post1.getTitle())))
        .andExpect(jsonPath("$._embedded.postDtoList[1].title", is(post2.getTitle())))
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void getAllPostsForSubreddit_ThrowsSubredditNotFoundException_WhenSubredditDoesNotExist() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/subreddit/{subredditId}", 0)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Subreddit with id 0 is not found.")));
  }

  @Test
  void getPostsForUser_ReturnsPosts_WhenUserExists() throws Exception {
    Post post1 = new Post(userId, "I love you.", "I do.", subredditId);
    Post post2 = new Post(userId, "What's up.", "Not much.", subredditId);
    postRepository.saveAll(List.of(post1, post2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/user/{username}", "username")
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].postDtoList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.postDtoList[0].title", is(post1.getTitle())))
        .andExpect(jsonPath("$._embedded.postDtoList[1].title", is(post2.getTitle())))
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void getPostsForUser_ThrowsUserNotFoundException_WhenUserDoesNotExist() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/posts/user/{username}", "janinedoe")
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("User with username janinedoe is not found.")));
  }

  @Test
  void create_ReturnsNewPost() throws Exception {
    Post post = new Post(userId, "I love you.", "I do.", subredditId);
    CreatePostRequest postDto = new CreatePostRequest(subredditId, post.getTitle(), post.getText());
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(postDto);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/posts")
            .header("AUTHORIZATION", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.title", is(post.getTitle())))
        .andExpect(jsonPath("$.text", is(post.getText())))
        .andExpect(jsonPath("$.subreddit", is(subredditId.intValue())))
        .andExpect(jsonPath("$.user", is(userId.intValue())));
  }

  @Test
  void create_ThrowsSubredditNotFoundException_WhenSubredditDoesNotExist() throws Exception {
    Post post = new Post(userId, "I love you.", "I do.", subredditId);
    CreatePostRequest postDto = new CreatePostRequest(0L, post.getTitle(), post.getText());
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(postDto);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Subreddit with id 0 is not found.")));
  }

  @Test
  void create_ThrowsDtoValidationException_WhenInvalidDto() throws Exception {
    CreatePostRequest postDto = new CreatePostRequest();
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(postDto);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/posts")
            .header("AUTHORIZATION", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value(containsString("subredditId")))
        .andExpect(jsonPath("$.message").value(containsString("title")))
        .andExpect(jsonPath("$.message").value(containsString("text")));
  }

  @Test
  void delete_ReturnsDeletedPost() throws Exception {
    Post post = new Post(userId, "I love you.", "I do.", subredditId);
    post.setVotes(3);
    post.setCommentsCounter(5);
    postRepository.save(post);

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/{postId}", post.getId())
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.id", is(post.getId().intValue())))
        .andExpect(jsonPath("$.title", is(post.getTitle())))
        .andExpect(jsonPath("$.text", is(post.getText())))
        .andExpect(jsonPath("$.subreddit", is(subredditId.intValue())))
        .andExpect(jsonPath("$.user", is(userId.intValue())))
        .andExpect(jsonPath("$.votes", is(post.getVotes())))
        .andExpect(jsonPath("$.commentsCounter", is(post.getCommentsCounter())));
  }

  @Test
  void delete_ThrowsPostNotFoundException_WhenPostDoesNotExist() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/{postId}", 0)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Post with id 0 is not found.")));
  }

  @Test
  void update_ReturnsUpdatedPost() throws Exception {
    Post post = new Post(userId, "I love you.", "I do.", subredditId);
    postRepository.save(post);
    UpdatePostRequest postDto = new UpdatePostRequest(post.getId(), post.getTitle(), post.getText());
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(postDto);

    mockMvc.perform(MockMvcRequestBuilders.put("/api/posts")
            .header("AUTHORIZATION", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(valueAsString))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.id", is(post.getId().intValue())))
        .andExpect(jsonPath("$.title", is(post.getTitle())))
        .andExpect(jsonPath("$.text", is(post.getText())))
        .andExpect(jsonPath("$.subreddit", is(subredditId.intValue())))
        .andExpect(jsonPath("$.user", is(userId.intValue())))
        .andExpect(jsonPath("$.votes", is(post.getVotes())))
        .andExpect(jsonPath("$.commentsCounter", is(post.getCommentsCounter())));
  }

  @Test
  void update_ThrowsPostNotFoundException_WhenPostDoesNotExist() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/posts/{postId}", 0)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Post with id 0 is not found.")));
  }

  @Test
  void update_ThrowsDtoValidationException_WhenUpdatePostDtoIsInvalid() throws Exception {
    UpdatePostRequest postDto = new UpdatePostRequest(null, "Todays News", "Something new");
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(postDto);

    mockMvc.perform(MockMvcRequestBuilders.put("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .header("AUTHORIZATION", "Bearer " + jwt)
            .content(valueAsString))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            jsonPath("$.message", is("DTO validation failed for the following fields: postId.")));
  }
}