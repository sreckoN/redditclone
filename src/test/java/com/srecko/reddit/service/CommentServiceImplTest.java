package com.srecko.reddit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.srecko.reddit.dto.CommentDto;
import com.srecko.reddit.dto.UserMediator;
import com.srecko.reddit.dto.requests.CommentRequest;
import com.srecko.reddit.entity.Comment;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.comment.CommentNotFoundException;
import com.srecko.reddit.exception.post.PostNotFoundException;
import com.srecko.reddit.exception.user.UserNotFoundException;
import com.srecko.reddit.repository.CommentRepository;
import com.srecko.reddit.repository.PostRepository;
import com.srecko.reddit.repository.UserRepository;
import com.srecko.reddit.service.utils.TestConfig;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CommentServiceImpl.class, TestConfig.class})
@ExtendWith(SpringExtension.class)
class CommentServiceImplTest {

  @MockBean
  private CommentRepository commentRepository;

  @Autowired
  private CommentServiceImpl commentServiceImpl;

  @MockBean
  private PostRepository postRepository;

  @MockBean
  private UserRepository userRepository;

  @Autowired
  private final ModelMapper modelMapper = new ModelMapper();

  private User user;
  private Post post;
  private Comment comment;

  @BeforeEach
  void setUp() {
    user = new User("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou", "GB", true);
    user.setId(123L);
    user.setComments(new ArrayList<>());
    user.setPosts(new ArrayList<>());
    LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
    user.setRegistrationDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));

    Subreddit subreddit = new Subreddit("Name", "The characteristics of someone or something",
        user);
    LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
    subreddit.setCreatedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
    subreddit.setId(123L);
    subreddit.setPosts(new ArrayList<>());

    post = new Post(user, "New Post", "This is a new post", subreddit);
    post.setComments(new ArrayList<>());
    post.setCommentsCounter(1);
    LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
    post.setDateOfCreation(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
    post.setId(123L);
    user.getPosts().add(post);
    subreddit.getPosts().add(post);

    comment = new Comment(user, "I can't believe", post);
    LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
    comment.setCreated(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
    comment.setId(123L);
    comment.setVotes(1);
    user.getComments().add(comment);
    post.getComments().add(comment);
  }

  @Test
  void testGetAllCommentsForPost_ReturnsComments() {
    // given
    Comment comment2 = new Comment(user, "Yeah, me neither", post);
    comment2.setId(222L);
    post.getComments().add(comment2);
    given(postRepository.findById(any())).willReturn(Optional.ofNullable(post));
    given(commentRepository.findAllByPost(any(), any())).willReturn(new PageImpl<>(List.of(comment, comment2)));
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "text"));

    // when
    Page<CommentDto> page = commentServiceImpl.getAllCommentsForPost(post.getId(), pageRequest);

    // then
    assertEquals(2, page.getTotalElements());
    assertTrue(page.getContent().contains(modelMapper.map(comment, CommentDto.class)));
    assertTrue(page.getContent().contains(modelMapper.map(comment2, CommentDto.class)));
  }

  @Test
  void testGetAllCommentsForPost_ThrowsPostNotFoundException_WhenPostDoesNotExist() {
    // given
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "text"));

    // when then
    assertThrows(PostNotFoundException.class, () ->
        commentServiceImpl.getAllCommentsForPost(post.getId(), pageRequest));
  }

  @Test
  void getAllCommentsForUsername_ReturnsComments() {
    // given
    Comment comment2 = new Comment(user, "Yeah, me neither", post);
    comment2.setId(222L);
    user.getComments().add(comment2);
    given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));
    given(commentRepository.findAllByUser(any(), any()))
        .willReturn(new PageImpl<>(List.of(comment, comment2)));
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "text"));

    // when
    Page<CommentDto> page = commentServiceImpl.getAllCommentsForUsername(
        user.getUsername(), pageRequest);

    // then
    assertEquals(2, page.getTotalElements());
    List<CommentDto> content = page.getContent();
    CommentDto map1 = modelMapper.map(comment, CommentDto.class);
    assertTrue(content.contains(map1));
    CommentDto map2 = modelMapper.map(comment2, CommentDto.class);
    assertTrue(content.contains(map2));
  }

  @Test
  void getAllCommentsForUsername_ThrowsUserNotFoundException_WhenUserDoesNotExist() {
    // given
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "text"));

    // when then
    assertThrows(UserNotFoundException.class, () ->
        commentServiceImpl.getAllCommentsForUsername(user.getUsername(), pageRequest));
  }

  @Test
  void saveComment_ReturnsSavedComment_WhenSuccessfullySaved() {
    // given
    given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));
    given(postRepository.findById(any())).willReturn(Optional.ofNullable(post));

    Authentication authentication = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    UserMediator userMediator = new UserMediator(user);
    when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(
        userMediator);

    // when
    CommentDto savedComment = commentServiceImpl.save(
        new CommentRequest(comment.getText(), comment.getPost().getId()));

    // then
    assertEquals(comment.getText(), savedComment.getText());
    assertEquals(comment.getUser(), savedComment.getUser());
    assertEquals(comment.getPost(), savedComment.getPost());
  }

  @Test
  void saveComment_ThrowsUserNotFoundException_WhenUserDoesNotExist() {
    // given
    Authentication authentication = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    UserMediator userMediator = new UserMediator(user);
    when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(
        userMediator);

    // when then
    assertThrows(UserNotFoundException.class, () ->
        commentServiceImpl.save(new CommentRequest(comment.getText(), comment.getPost().getId())));
  }

  @Test
  void saveComment_ThrowsPostNotFoundException_WhenPostDoesNotExist() {
    // given
    given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));

    Authentication authentication = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    UserMediator userMediator = new UserMediator(user);
    when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(
        userMediator);

    // when then
    assertThrows(PostNotFoundException.class, () ->
        commentServiceImpl.save(new CommentRequest(comment.getText(), comment.getPost().getId())));
  }

  @Test
  void delete_ReturnsDeletedComment_WhenSuccessfullyDeleted() {
    // given
    given(commentRepository.findById(any())).willReturn(Optional.ofNullable(comment));

    // when
    CommentDto deleted = commentServiceImpl.delete(comment.getId());

    // then
    assertNotNull(deleted);
    assertEquals(comment.getId(), deleted.getId());
    assertEquals(comment.getText(), deleted.getText());
    assertEquals(comment.getUser(), deleted.getUser());
    assertEquals(comment.getPost(), deleted.getPost());
  }

  @Test
  void delete_ThrowsCommentNotFoundException_WhenCommentNotFound() {
    // given when then
    assertThrows(CommentNotFoundException.class, () -> commentServiceImpl.delete(comment.getId()));
  }
}