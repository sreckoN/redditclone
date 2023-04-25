package com.srecko.reddit.votes.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srecko.reddit.controller.utils.JwtTestUtils;
import com.srecko.reddit.controller.utils.WithMockCustomUser;
import com.srecko.reddit.votes.dto.VoteCommentRequest;
import com.srecko.reddit.votes.dto.VotePostRequest;
import com.srecko.reddit.votes.dto.VoteRequest;
import com.srecko.reddit.votes.entity.VoteComment;
import com.srecko.reddit.votes.entity.VotePost;
import com.srecko.reddit.votes.entity.VoteType;
import com.srecko.reddit.votes.repository.VoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class VoteControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private VoteRepository voteRepository;

  private final String jwt = JwtTestUtils.getJwt();

  private Long userId;

  private Long postId;

  private Long commentId;

  @BeforeEach
  void setUp() {
    voteRepository.deleteAll();
    userId = 123L;
    postId = 123L;
    commentId = 123L;
  }

  @AfterEach
  void tearDown() {
    voteRepository.deleteAll();
  }

  @Test
  @WithMockCustomUser
  void savePostVote_ReturnsVote_WhenSuccessfullyCreated() throws Exception {
    VotePost vote = new VotePost(userId, VoteType.UPVOTE, postId);
    VotePostRequest voteDto = new VotePostRequest(vote.getType(), postId);
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(voteDto);
    mockMvc.perform(MockMvcRequestBuilders.post("/api/votes/post")
            .contentType(APPLICATION_JSON)
            .content(valueAsString)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.post", is(vote.getPostId().intValue())))
        .andExpect(jsonPath("$.user", is(vote.getUserId().intValue())))
        .andExpect(jsonPath("$.type", is(VoteType.UPVOTE.toString())));
  }

  @Test
  @WithMockCustomUser
  void savePostVote_ThrowsPostNotFoundException_WhenPostDoesNotExist() throws Exception {
    VoteRequest voteRequest = new VotePostRequest(VoteType.UPVOTE, 0L);
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(voteRequest);
    mockMvc.perform(MockMvcRequestBuilders.post("/api/votes/post")
            .contentType(APPLICATION_JSON)
            .content(valueAsString)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Post with id 0 is not found.")));
  }

  @Test
  void savePostVote_ThrowsDtoValidationException_WhenInvalidDtoProvided() throws Exception {
    VoteRequest voteRequest = new VotePostRequest(VoteType.UPVOTE, null);
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(voteRequest);
    mockMvc.perform(MockMvcRequestBuilders.post("/api/votes/post")
            .contentType(APPLICATION_JSON)
            .content(valueAsString)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(
            jsonPath("$.message", is("DTO validation failed for the following fields: postId.")));
  }

  @Test
  @WithMockCustomUser
  void saveCommentVote_ReturnsVote_WhenSuccessfullySaved() throws Exception {
    VoteComment vote = new VoteComment(userId, VoteType.UPVOTE, commentId);
    VoteCommentRequest voteDto = new VoteCommentRequest(vote.getType(), commentId);
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(voteDto);
    mockMvc.perform(MockMvcRequestBuilders.post("/api/votes/comment")
            .contentType(APPLICATION_JSON)
            .content(valueAsString)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.comment", is(vote.getCommentId().intValue())))
        .andExpect(jsonPath("$.user", is(vote.getUserId().intValue())))
        .andExpect(jsonPath("$.type", is(VoteType.UPVOTE.toString())));
  }

  @Test
  @WithMockCustomUser
  void saveCommentVote_ThrowsCommentNotFoundException_WhenCommentDoesNotExist() throws Exception {
    VoteRequest voteRequest = new VoteCommentRequest(VoteType.UPVOTE, 0L);
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(voteRequest);
    mockMvc.perform(MockMvcRequestBuilders.post("/api/votes/comment")
            .contentType(APPLICATION_JSON)
            .content(valueAsString)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Comment with id 0 is not found.")));
  }

  @Test
  void saveCommentVote_ThrowsDtoValidationException_WhenInvalidDtoProvided() throws Exception {
    VoteRequest voteRequest = new VoteCommentRequest(VoteType.UPVOTE, null);
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(voteRequest);
    mockMvc.perform(MockMvcRequestBuilders.post("/api/votes/comment")
            .contentType(APPLICATION_JSON)
            .content(valueAsString)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.message",
            is("DTO validation failed for the following fields: commentId.")));
  }

  @Test
  @WithMockCustomUser
  void deletePostVote_ReturnsDeletedVote_WhenSuccessfullyDeleted() throws Exception {
    VotePost vote = new VotePost(userId, VoteType.UPVOTE, postId);
    voteRepository.save(vote);
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/votes/post/{voteId}", vote.getId())
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.id", is(vote.getId().intValue())));
  }

  @Test
  @WithMockCustomUser
  void deletePostVote_ThrowsVoteNotFoundException_WhenVoteNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/votes/post/{voteId}", 0)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Vote with id 0 is not found.")));
  }

  @Test
  @WithMockCustomUser
  void deleteCommentVote_ReturnDeletedVote_WhenSuccessfullyDeleted() throws Exception {
    VoteComment vote = new VoteComment(userId, VoteType.UPVOTE, commentId);
    voteRepository.save(vote);
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/votes/comment/{voteId}", vote.getId())
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.id", is(vote.getId().intValue())));
  }

  @Test
  @WithMockCustomUser
  void deleteCommentVote_ThrowsVoteNotFoundException_WhenVoteNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/votes/comment/{voteId}", 0)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Vote with id 0 is not found.")));
  }
}