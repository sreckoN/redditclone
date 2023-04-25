package com.srecko.reddit.comments.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srecko.reddit.comments.dto.CommentRequest;
import com.srecko.reddit.comments.entity.Comment;
import com.srecko.reddit.comments.repository.CommentRepository;
import com.srecko.reddit.controller.utils.JwtTestUtils;
import com.srecko.reddit.controller.utils.WithMockCustomUser;
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
class CommentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private CommentRepository commentRepository;

  private final String jwt = JwtTestUtils.getJwt();

  private Long userId;

  private Long postId;

  @BeforeEach
  void setUp() {
    commentRepository.deleteAll();
    userId = 123L;
    postId = 123L;
  }

  @AfterEach
  void tearDown() {
    commentRepository.deleteAll();
  }

  @Test
  void getCommentsForPost_ReturnsComments() throws Exception {
    Comment comment1 = new Comment(userId, "Good", postId);
    Comment comment2 = new Comment(userId, "Not bad", postId);
    commentRepository.saveAll(List.of(comment1, comment2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/comments/post/{postId}", postId)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].commentDtoList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.commentDtoList[0].text", is(comment1.getText())))
        .andExpect(jsonPath("$._embedded.commentDtoList[1].text", is(comment2.getText())))
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
    Comment comment1 = new Comment(userId, "Good", postId);
    Comment comment2 = new Comment(userId, "Not bad", postId);
    commentRepository.saveAll(List.of(comment1, comment2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/comments/user/{username}", userId)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].commentDtoList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.commentDtoList[0].text", is(comment1.getText())))
        .andExpect(jsonPath("$._embedded.commentDtoList[1].text", is(comment2.getText())))
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
    Comment comment = new Comment(userId, "Good", postId);
    commentRepository.save(comment);

    CommentRequest commentRequest = new CommentRequest(comment.getText(), postId);
    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writeValueAsString(commentRequest);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/comments")
            .header("AUTHORIZATION", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.text", is(comment.getText())))
        .andExpect(jsonPath("$.votes", is(comment.getVotes())))
        .andExpect(jsonPath("$.post", is(comment.getPostId().intValue())))
        .andExpect(jsonPath("$.user", is(comment.getUserId().intValue())));
  }

  @Test
  @WithMockCustomUser
  void createComment_ThrowsPostNotFoundException_WhenPostDoesNotExist() throws Exception {
    CommentRequest commentRequest = new CommentRequest("New comment", 0L);
    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writeValueAsString(commentRequest);

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
    CommentRequest commentRequest = new CommentRequest();
    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writeValueAsString(commentRequest);

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
    Comment comment = new Comment(userId, "Good", postId);
    commentRepository.save(comment);

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/comments/{commentId}", comment.getId())
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.id", is(comment.getId().intValue())))
        .andExpect(jsonPath("$.text", is(comment.getText())))
        .andExpect(jsonPath("$.votes", is(comment.getVotes())))
        .andExpect(jsonPath("$.post", is(comment.getPostId().intValue())));
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
    Comment comment = new Comment(userId, "Good", postId);
    commentRepository.save(comment);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/comments/{commentId}", comment.getId())
        .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.id", is(comment.getId().intValue())))
        .andExpect(jsonPath("$.text", is(comment.getText())))
        .andExpect(jsonPath("$.votes", is(comment.getVotes())))
        .andExpect(jsonPath("$.post", is(comment.getPostId().intValue())));
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
    Comment comment1 = new Comment(userId, "Good", postId);
    Comment comment2 = new Comment(userId, "Not bad", postId);
    commentRepository.saveAll(List.of(comment1, comment2));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/comments")
        .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].commentDtoList", hasSize(2)))
        .andExpect(jsonPath("$._embedded.commentDtoList[0].text", is(comment1.getText())))
        .andExpect(jsonPath("$._embedded.commentDtoList[1].text", is(comment2.getText())))
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }
}