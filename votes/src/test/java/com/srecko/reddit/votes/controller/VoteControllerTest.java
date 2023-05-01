package com.srecko.reddit.votes.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srecko.reddit.votes.dto.VoteCommentRequest;
import com.srecko.reddit.votes.dto.VotePostRequest;
import com.srecko.reddit.votes.dto.VoteRequest;
import com.srecko.reddit.votes.entity.VoteComment;
import com.srecko.reddit.votes.entity.VotePost;
import com.srecko.reddit.votes.entity.VoteType;
import com.srecko.reddit.votes.repository.VoteRepository;
import com.srecko.reddit.votes.service.client.CommentsFeignClient;
import com.srecko.reddit.votes.service.client.PostsFeignClient;
import com.srecko.reddit.votes.service.client.UsersFeignClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

  @MockBean
  private UsersFeignClient usersFeignClient;

  @MockBean
  private PostsFeignClient postsFeignClient;

  @MockBean
  private CommentsFeignClient commentsFeignClient;

  //private final String jwt = JwtTestUtils.getJwt();
  private final String jwt = "djanasljfhd.djhfbjadhbfjsd.dsjhnFDFNJKDJ";

  private Long userId;

  private Long postId;

  private Long commentId;

  @BeforeEach
  void setUp() {
    userId = 123L;
    postId = 123L;
    commentId = 123L;
    voteRepository.deleteAll();
  }

  @AfterEach
  void tearDown() {
    voteRepository.deleteAll();
  }

  @Test
  void savePostVote_ReturnsVote_WhenSuccessfullyCreated() throws Exception {
    given(usersFeignClient.getUserId("username")).willReturn(userId);
    doNothing().when(postsFeignClient).checkIfPostExists(any());

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
        .andExpect(jsonPath("$.postId", is(vote.getPostId().intValue())))
        .andExpect(jsonPath("$.userId", is(vote.getUserId().intValue())))
        .andExpect(jsonPath("$.type", is(VoteType.UPVOTE.toString())));
  }

  @Test
  void savePostVote_ThrowsDtoValidationException_WhenInvalidDtoProvided() throws Exception {
    given(usersFeignClient.getUserId(any())).willReturn(userId);
    doNothing().when(postsFeignClient).checkIfPostExists(any());

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
  void saveCommentVote_ReturnsVote_WhenSuccessfullySaved() throws Exception {
    given(usersFeignClient.getUserId(any())).willReturn(userId);
    doNothing().when(commentsFeignClient).checkIfCommentExists(any());

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
        .andExpect(jsonPath("$.commentId", is(vote.getCommentId().intValue())))
        .andExpect(jsonPath("$.userId", is(vote.getUserId().intValue())))
        .andExpect(jsonPath("$.type", is(VoteType.UPVOTE.toString())));
  }

  @Test
  void saveCommentVote_ThrowsDtoValidationException_WhenInvalidDtoProvided() throws Exception {
    given(usersFeignClient.getUserId(any())).willReturn(userId);
    doNothing().when(commentsFeignClient).checkIfCommentExists(any());

    VoteRequest voteRequest = new VoteCommentRequest(null, null);
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(voteRequest);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/votes/comment")
            .contentType(APPLICATION_JSON)
            .content(valueAsString)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.message",
            is("DTO validation failed for the following fields: commentId, type.")));
  }

  @Test
  void deletePostVote_ReturnsDeletedVote_WhenSuccessfullyDeleted() throws Exception {
    doNothing().when(postsFeignClient).checkIfPostExists(any());

    VotePost vote = new VotePost(userId, VoteType.UPVOTE, postId);
    voteRepository.save(vote);

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/votes/post/{voteId}", vote.getId())
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.id", is(vote.getId().intValue())));
  }

  @Test
  void deletePostVote_ThrowsVoteNotFoundException_WhenVoteNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/votes/post/{voteId}", 0)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Vote with id 0 is not found.")));
  }

  @Test
  void deleteCommentVote_ReturnDeletedVote_WhenSuccessfullyDeleted() throws Exception {
    doNothing().when(commentsFeignClient).checkIfCommentExists(any());

    VoteComment vote = new VoteComment(userId, VoteType.UPVOTE, commentId);
    voteRepository.save(vote);

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/votes/comment/{voteId}", vote.getId())
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaTypes.HAL_JSON))
        .andExpect(jsonPath("$.id", is(vote.getId().intValue())));
  }

  @Test
  void deleteCommentVote_ThrowsVoteNotFoundException_WhenVoteNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/votes/comment/{voteId}", 0)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.message", is("Vote with id 0 is not found.")));
  }
}