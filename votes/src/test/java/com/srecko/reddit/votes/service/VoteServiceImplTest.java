package com.srecko.reddit.votes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.srecko.reddit.votes.dto.VoteCommentDto;
import com.srecko.reddit.votes.dto.VoteCommentRequest;
import com.srecko.reddit.votes.dto.VotePostDto;
import com.srecko.reddit.votes.dto.VotePostRequest;
import com.srecko.reddit.votes.entity.VoteComment;
import com.srecko.reddit.votes.entity.VotePost;
import com.srecko.reddit.votes.entity.VoteType;
import com.srecko.reddit.votes.exception.VoteNotFoundException;
import com.srecko.reddit.votes.repository.VoteRepository;
import com.srecko.reddit.votes.service.client.CommentsFeignClient;
import com.srecko.reddit.votes.service.client.PostsFeignClient;
import com.srecko.reddit.votes.service.client.UsersFeignClient;
import com.srecko.reddit.votes.service.utils.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {VoteServiceImpl.class, TestConfig.class})
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class VoteServiceImplTest {

  @MockBean
  private VoteRepository voteRepository;

  @MockBean
  private UsersFeignClient usersFeignClient;

  @MockBean
  private PostsFeignClient postsFeignClient;

  @MockBean
  private CommentsFeignClient commentsFeignClient;

  @Autowired
  private VoteService voteService;

  private Long userId;

  private Long postId;

  private Long commentId;

  private final ModelMapper modelMapper = new ModelMapper();

  @BeforeEach
  void setUp() {
    userId = 123L;
    postId = 123L;
    commentId = 123L;
  }

  @Test
  void savePostVote_ReturnsPostVote_WhenSuccessfullySaved() {
    // given
    /*given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));
    given(postRepository.findById(any())).willReturn(Optional.ofNullable(post));

    Authentication authentication = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    UserMediator userMediator = new UserMediator(user);
    when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(
        userMediator);*/

    // when
    VotePostDto saved = voteService.savePostVote(new VotePostRequest(VoteType.UPVOTE, postId));

    // then
    assertNotNull(saved);
    // assertEquals(1, post.getVotes());
  }

  @Test
  void saveCommentVote_ReturnsCommentVote_WhenSuccessfullySaved() {
    // given
    /*given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));
    given(commentRepository.findById(any())).willReturn(Optional.ofNullable(comment));

    Authentication authentication = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    UserMediator userMediator = new UserMediator(user);
    when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(
        userMediator);*/

    // when
    VoteCommentDto saved = voteService.saveCommentVote(new VoteCommentRequest(VoteType.UPVOTE, commentId));

    // then
    assertNotNull(saved);
    // assertEquals(2, comment.getVotes());
  }

  @Test
  void deletePostVote_ReturnsPostVote_WhenSuccessfullyDeleted() {
    // given
    VotePost vote = new VotePost(userId, VoteType.UPVOTE, postId);
    // given(voteRepository.findById(any())).willReturn(Optional.of(vote));

    // when
    VotePostDto deleted = voteService.deletePostVote(vote.getId());

    // then
    assertNotNull(deleted);
    assertEquals(vote.getUserId(), deleted.getUserId());
    assertEquals(vote.getPostId(), deleted.getPostId());
    assertEquals(vote.getType(), deleted.getType());
  }

  @Test
  void deletePostVote_ThrowsVoteNotFoundException_WhenVoteDoesNotExist() {
    // given when then
    assertThrows(VoteNotFoundException.class, () -> {
      voteService.deletePostVote(1L);
    });
  }

  @Test
  void deleteCommentVote_ReturnsCommentVote_WhenSuccessfullyDeleted() {
    // given
    VoteComment vote = new VoteComment(userId, VoteType.UPVOTE, commentId);
    // given(voteRepository.findById(any())).willReturn(Optional.of(vote));

    // when
    VoteCommentDto deleted = voteService.deleteCommentVote(vote.getId());

    // then
    assertNotNull(deleted);
    assertEquals(vote.getUserId(), deleted.getUserId());
    assertEquals(vote.getCommentId(), deleted.getCommentId());
    assertEquals(vote.getType(), deleted.getType());
  }

  @Test
  void deleteCommentVote_ThrowsVoteNotFoundException_WhenVoteDoesNotExist() {
    // given when then
    assertThrows(VoteNotFoundException.class, () -> {
      voteService.deleteCommentVote(1L);
    });
  }
}