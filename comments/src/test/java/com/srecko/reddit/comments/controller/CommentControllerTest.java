package com.srecko.reddit.comments.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srecko.reddit.comments.dto.CommentRequest;
import com.srecko.reddit.comments.entity.Comment;
import com.srecko.reddit.comments.entity.CommentParentType;
import com.srecko.reddit.comments.repository.CommentRepository;
import com.srecko.reddit.comments.service.client.PostsFeignClient;
import com.srecko.reddit.comments.service.client.UsersFeignClient;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

  @MockBean
  private UsersFeignClient usersFeignClient;

  @MockBean
  private PostsFeignClient postsFeignClient;

  // private final String jwt = JwtTestUtils.getJwt();
  private final String jwt = "dfgegoidsaid.sdvnsdOVNISD.Djvndovisdn";

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
    Comment comment1 = new Comment(userId, "Good", CommentParentType.POST, postId);
    Comment comment2 = new Comment(userId, "Not bad", CommentParentType.POST, postId);
    commentRepository.saveAll(List.of(comment1, comment2));

    doNothing().when(postsFeignClient).checkIfPostExists(any());

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
  void getCommentsForComment_ReturnsComments() throws Exception {
    Comment comment1 = new Comment(userId, "Good", CommentParentType.POST, postId);
    commentRepository.save(comment1);
    Comment comment2 = new Comment(userId, "Not bad", CommentParentType.COMMENT, comment1.getId());
    commentRepository.save(comment2);

    doNothing().when(postsFeignClient).checkIfPostExists(any());

    mockMvc.perform(MockMvcRequestBuilders.get("/api/comments/comment/{commentId}", comment1.getId())
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.['_embedded'].commentDtoList", hasSize(1)))
        .andExpect(jsonPath("$._embedded.commentDtoList[0].text", is(comment2.getText())))
        .andExpect(jsonPath("$._links.self").exists())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.size", is(10)))
        .andExpect(jsonPath("$.page.totalElements", is(1)))
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void getCommentsForUser_ReturnsComments() throws Exception {
    Comment comment1 = new Comment(userId, "Good", CommentParentType.POST, postId);
    Comment comment2 = new Comment(userId, "Not bad", CommentParentType.POST, postId);
    commentRepository.saveAll(List.of(comment1, comment2));

    doNothing().when(usersFeignClient).checkIfExists(any());

    mockMvc.perform(MockMvcRequestBuilders.get("/api/comments/user/{userId}", userId)
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
  // @WithMockCustomUser
  void createComment_ReturnsCreatedComment_WhenSuccessfullyCreated() throws Exception {
    Comment comment = new Comment(userId, "Good", CommentParentType.POST, postId);
    commentRepository.save(comment);

    CommentRequest commentRequest = new CommentRequest(comment.getText(), comment.getParentType(), comment.getParentId());
    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writeValueAsString(commentRequest);

    given(usersFeignClient.getUserId(any())).willReturn(userId);
    doNothing().when(postsFeignClient).checkIfPostExists(any());

    mockMvc.perform(MockMvcRequestBuilders.post("/api/comments")
            .header("AUTHORIZATION", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.text", is(comment.getText())))
        .andExpect(jsonPath("$.votes", is(comment.getVotes())))
        .andExpect(jsonPath("$.parentId", is(comment.getParentId().intValue())))
        .andExpect(jsonPath("$.userId", is(comment.getUserId().intValue())));
  }

  @Test
  // @WithMockCustomUser
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
        .andExpect(jsonPath("$.message").value(containsString("parentType")))
        .andExpect(jsonPath("$.message").value(containsString("parentId")));
  }

  @Test
  void deleteComment_ReturnsDeletedComment_WhenSuccessfullyDeleted() throws Exception {
    Comment comment = new Comment(userId, "Good", CommentParentType.POST, postId);
    commentRepository.save(comment);

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/comments/{commentId}", comment.getId())
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.id", is(comment.getId().intValue())))
        .andExpect(jsonPath("$.text", is(comment.getText())))
        .andExpect(jsonPath("$.votes", is(comment.getVotes())))
        .andExpect(jsonPath("$.parentId", is(comment.getParentId().intValue())));
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
    Comment comment = new Comment(userId, "Good", CommentParentType.POST, postId);
    commentRepository.save(comment);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/comments/{commentId}", comment.getId())
        .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.id", is(comment.getId().intValue())))
        .andExpect(jsonPath("$.text", is(comment.getText())))
        .andExpect(jsonPath("$.votes", is(comment.getVotes())))
        .andExpect(jsonPath("$.parentId", is(comment.getParentId().intValue())));
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
    Comment comment1 = new Comment(userId, "Good", CommentParentType.POST, postId);
    Comment comment2 = new Comment(userId, "Not bad", CommentParentType.POST, postId);
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

  @Test
  void checkIfExists_Returns200_IfCommentExists() throws Exception {
    Comment comment = new Comment(userId, "Good", CommentParentType.POST, postId);
    commentRepository.save(comment);

    mockMvc.perform(MockMvcRequestBuilders.head("/api/comments/checkIfExists")
        .contentType(MediaType.APPLICATION_JSON)
        .content(comment.getId().toString()))
        .andExpect(status().isOk());
  }

  @Test
  void checkIfExists_ThrowsCommentNotFoundException_IfCommentDoesNotExist() throws Exception {
    long id = 123L;

    mockMvc.perform(MockMvcRequestBuilders.head("/api/comments/checkIfExists")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Long.toString(id)))
        .andExpect(status().is4xxClientError());
  }

  @Test
  void search_ReturnsPagedModelOfComments_WhenTheyMatchQuery() throws Exception {
    Comment comment1 = new Comment(userId, "Good", CommentParentType.POST, postId);
    Comment comment2 = new Comment(userId, "Very good", CommentParentType.POST, postId);
    commentRepository.saveAll(List.of(comment1, comment2));
    String query = "good";

    mockMvc.perform(MockMvcRequestBuilders.post("/api/comments/search")
        .contentType(MediaType.APPLICATION_JSON)
        .content(query))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$._embedded.commentDtoList").exists())
        .andExpect(jsonPath("$._embedded.commentDtoList[0].id", is(comment1.getId().intValue())))
        .andExpect(jsonPath("$._embedded.commentDtoList[0].userId", is(comment1.getUserId().intValue())))
        .andExpect(jsonPath("$._embedded.commentDtoList[1].id", is(comment2.getId().intValue())))
        .andExpect(jsonPath("$._embedded.commentDtoList[1].userId", is(comment2.getUserId().intValue())))
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.totalElements").exists())
        .andExpect(jsonPath("$.page.totalElements", is(2)))
        .andExpect(jsonPath("$.page.totalPages").exists())
        .andExpect(jsonPath("$.page.totalPages", is(1)));
  }

  @Test
  void search_ReturnsEmptyPagedModel_WhenNoCommentsMatchQuery() throws Exception {
    String query = "good";

    mockMvc.perform(MockMvcRequestBuilders.post("/api/comments/search")
            .contentType(MediaType.APPLICATION_JSON)
            .content(query))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$._embedded.commentDtoList").doesNotExist())
        .andExpect(jsonPath("$.page").exists())
        .andExpect(jsonPath("$.page.totalElements").exists())
        .andExpect(jsonPath("$.page.totalElements", is(0)))
        .andExpect(jsonPath("$.page.totalPages").exists())
        .andExpect(jsonPath("$.page.totalPages", is(0)));
  }
}