package com.srecko.reddit.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srecko.reddit.controller.utils.JwtTestUtils;
import com.srecko.reddit.controller.utils.WithMockCustomUser;
import com.srecko.reddit.dto.CommentDto;
import com.srecko.reddit.entity.Comment;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.repository.CommentRepository;
import com.srecko.reddit.repository.PostRepository;
import com.srecko.reddit.repository.SubredditRepository;
import com.srecko.reddit.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

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
  private CommentRepository commentRepository;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private SubredditRepository subredditRepository;

  @Autowired
  private UserRepository userRepository;

  private final String jwt = JwtTestUtils.getJwt();

  private User user;

  private Subreddit subreddit;

  private Post post;

  @BeforeEach
  void setUp() {
    commentRepository.deleteAll();
    postRepository.deleteAll();
    subredditRepository.deleteAll();
    userRepository.deleteAll();
    user = new User("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou", "GB", true);
    userRepository.save(user);
    subreddit = new Subreddit("Serbia", "Serbia's official subreddit", user);
    subredditRepository.save(subreddit);
    post = new Post(user, "How's the weather?", "I'm curious", subreddit);
    postRepository.save(post);
  }

  @AfterEach
  void tearDown() {
    commentRepository.deleteAll();
    postRepository.deleteAll();
    subredditRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void getCommentsForPost_ReturnsComments() throws Exception {
    Comment comment1 = new Comment(user, "Good", post);
    Comment comment2 = new Comment(user, "Not bad", post);
    commentRepository.saveAll(List.of(comment1, comment2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/comments/post/{postId}", post.getId())
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].commentList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.commentList[0].text", is(comment1.getText())))
        .andExpect(jsonPath("$._embedded.commentList[1].text", is(comment2.getText())))
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void getCommentsForPost_ThrowsPostNotFoundException_WhenPostDoesNotExist() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/comments/post/{postId}", 0)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.message", is("Post with id 0 is not found.")));
  }

  @Test
  void getCommentsForUsername_ReturnsComments() throws Exception {
    Comment comment1 = new Comment(user, "Good", post);
    Comment comment2 = new Comment(user, "Not bad", post);
    commentRepository.saveAll(List.of(comment1, comment2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/comments/user/{username}", user.getUsername())
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].commentList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.commentList[0].text", is(comment1.getText())))
        .andExpect(jsonPath("$._embedded.commentList[1].text", is(comment2.getText())))
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void getCommentsForUsername_ThrowsUserNotFoundException_WhenUserDoesNotExist() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/comments/user/{username}", "jakedoe")
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.message", is("User with username jakedoe is not found.")));
  }

  @Test
  @WithMockCustomUser
  void createComment_ReturnsCreatedComment_WhenSuccessfullyCreated() throws Exception {
    Comment comment = new Comment(user, "Good", post);
    commentRepository.save(comment);

    CommentDto commentDto = new CommentDto(comment.getText(), post.getId());
    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writeValueAsString(commentDto);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/comments")
            .header("AUTHORIZATION", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.text", is(comment.getText())))
        .andExpect(jsonPath("$.votes", is(comment.getVotes())))
        .andExpect(jsonPath("$.post", is(comment.getPost().getId().intValue())))
        .andExpect(jsonPath("$.user", is(comment.getUser().getId().intValue())));
  }

  @Test
  @WithMockCustomUser
  void createComment_ThrowsPostNotFoundException_WhenPostDoesNotExist() throws Exception {
    CommentDto commentDto = new CommentDto("New comment", 0L);
    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writeValueAsString(commentDto);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/comments")
            .header("AUTHORIZATION", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.message", is("Post with id 0 is not found.")));
  }

  @Test
  @WithMockCustomUser
  void createComment_ThrowsDtoValidationException_WhenInvalidDto() throws Exception {
    CommentDto commentDto = new CommentDto();
    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writeValueAsString(commentDto);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/comments")
            .header("AUTHORIZATION", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value(containsString("text")))
        .andExpect(jsonPath("$.message").value(containsString("post")));
  }

  @Test
  void deleteComment_ReturnsDeletedComment_WhenSuccessfullyDeleted() throws Exception {
    Comment comment = new Comment(user, "Good", post);
    commentRepository.save(comment);

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/comments/{commentId}", comment.getId())
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(comment.getId().intValue())))
        .andExpect(jsonPath("$.text", is(comment.getText())))
        .andExpect(jsonPath("$.votes", is(comment.getVotes())))
        .andExpect(jsonPath("$.post", is(comment.getPost().getId().intValue())));
  }

  @Test
  void deleteComment_ThrowsCommentNotFoundException_WhenCommentDoesNotExist() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/comments/{commentId}", 0)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.message", is("Comment with id 0 is not found.")));
  }

  @Test
  void getComment_ReturnsComment() throws Exception {
    Comment comment = new Comment(user, "Good", post);
    commentRepository.save(comment);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/comments/{commentId}", comment.getId())
        .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id", is(comment.getId().intValue())))
        .andExpect(jsonPath("$.text", is(comment.getText())))
        .andExpect(jsonPath("$.votes", is(comment.getVotes())))
        .andExpect(jsonPath("$.post", is(comment.getPost().getId().intValue())));
  }

  @Test
  void getComment_ThrowsCommentNotFoundException_WhenCommentNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/comments/{commentId}", 0)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Comment with id 0 is not found.")));
  }

  @Test
  void getAllComments() throws Exception {
    Comment comment1 = new Comment(user, "Good", post);
    Comment comment2 = new Comment(user, "Not bad", post);
    commentRepository.saveAll(List.of(comment1, comment2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/comments")
        .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].commentList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.commentList[0].text", is(comment1.getText())))
        .andExpect(jsonPath("$._embedded.commentList[1].text", is(comment2.getText())))
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }
}