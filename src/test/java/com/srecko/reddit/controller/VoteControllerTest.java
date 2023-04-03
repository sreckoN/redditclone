package com.srecko.reddit.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.srecko.reddit.controller.utils.JwtTestUtils;
import com.srecko.reddit.controller.utils.WithMockCustomUser;
import com.srecko.reddit.dto.requests.VoteCommentRequest;
import com.srecko.reddit.dto.requests.VotePostRequest;
import com.srecko.reddit.dto.requests.VoteRequest;
import com.srecko.reddit.entity.Comment;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.entity.VoteComment;
import com.srecko.reddit.entity.VotePost;
import com.srecko.reddit.entity.VoteType;
import com.srecko.reddit.repository.CommentRepository;
import com.srecko.reddit.repository.PostRepository;
import com.srecko.reddit.repository.SubredditRepository;
import com.srecko.reddit.repository.UserRepository;
import com.srecko.reddit.repository.VoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
class VoteControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private VoteRepository voteRepository;

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

  private Comment comment;

  @BeforeEach
  void setUp() {
    voteRepository.deleteAll();
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
    comment = new Comment(user, "It's good", post);
    commentRepository.save(comment);
  }

  @AfterEach
  void tearDown() {
    voteRepository.deleteAll();
    commentRepository.deleteAll();
    postRepository.deleteAll();
    subredditRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  @WithMockCustomUser
  void savePostVote_ReturnsVote_WhenSuccessfullyCreated() throws Exception {
    VotePost vote = new VotePost(user, VoteType.UPVOTE, post);
    VotePostRequest voteDto = new VotePostRequest(vote.getType(), post.getId());
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(voteDto);
    mockMvc.perform(MockMvcRequestBuilders.post("/api/votes/post")
            .contentType(APPLICATION_JSON)
            .content(valueAsString)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.post", is(vote.getPost().getId().intValue())))
        .andExpect(jsonPath("$.user", is(vote.getUser().getId().intValue())))
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
    VoteComment vote = new VoteComment(user, VoteType.UPVOTE, comment);
    VoteCommentRequest voteDto = new VoteCommentRequest(vote.getType(), comment.getId());
    ObjectMapper objectMapper = new ObjectMapper();
    String valueAsString = objectMapper.writeValueAsString(voteDto);
    mockMvc.perform(MockMvcRequestBuilders.post("/api/votes/comment")
            .contentType(APPLICATION_JSON)
            .content(valueAsString)
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.comment", is(vote.getComment().getId().intValue())))
        .andExpect(jsonPath("$.user", is(vote.getUser().getId().intValue())))
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
    VotePost vote = new VotePost(user, VoteType.UPVOTE, post);
    voteRepository.save(vote);
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/votes/post/{voteId}", vote.getId())
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
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
    VoteComment vote = new VoteComment(user, VoteType.UPVOTE, comment);
    voteRepository.save(vote);
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/votes/comment/{voteId}", vote.getId())
            .header("AUTHORIZATION", "Bearer " + jwt))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
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