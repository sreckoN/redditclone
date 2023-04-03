package com.srecko.reddit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.srecko.reddit.dto.PostDto;
import com.srecko.reddit.dto.UserMediator;
import com.srecko.reddit.dto.requests.CreatePostRequest;
import com.srecko.reddit.dto.requests.UpdatePostRequest;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.post.PostNotFoundException;
import com.srecko.reddit.exception.subreddit.SubredditNotFoundException;
import com.srecko.reddit.exception.user.UserNotFoundException;
import com.srecko.reddit.repository.PostRepository;
import com.srecko.reddit.repository.SubredditRepository;
import com.srecko.reddit.repository.UserRepository;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {PostServiceImpl.class, TestConfig.class})
@ExtendWith(SpringExtension.class)
class PostServiceImplTest {

  @MockBean
  private PostRepository postRepository;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private SubredditRepository subredditRepository;

  @Autowired
  private PostService postService;

  private final ModelMapper modelMapper = new ModelMapper();

  private User user;
  private Subreddit subreddit;
  private Post post;

  @BeforeEach
  void setUp() {
    user = new User("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou", "GB", true);
    user.setId(123L);
    user.setComments(new ArrayList<>());
    user.setPosts(new ArrayList<>());
    LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
    user.setRegistrationDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));

    subreddit = new Subreddit("Name", "The characteristics of someone or something", user);
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
  }

  @Test
  void save_ReturnsSavedPost_WhenSuccessfullySaved() {
    // given
    given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));
    given(subredditRepository.findById(any())).willReturn(Optional.ofNullable(subreddit));

    Authentication authentication = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    UserMediator userMediator = new UserMediator(user);
    when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(
        userMediator);

    // when
    PostDto saved = postService.save(
        new CreatePostRequest(subreddit.getId(), post.getTitle(), post.getText()));

    // then
    assertNotNull(saved);
    assertEquals(post.getTitle(), saved.getTitle());
    assertEquals(post.getText(), saved.getText());
    assertEquals(post.getUser(), saved.getUser());
    assertEquals(post.getSubreddit(), saved.getSubreddit());
  }

  @Test
  void save_ThrowsSubredditNotFoundException_WhenSubredditDoesNotExist() {
    // given
    given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));

    Authentication authentication = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    UserMediator userMediator = new UserMediator(user);
    when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(
        userMediator);

    // when
    assertThrows(SubredditNotFoundException.class, () ->
        postService.save(new CreatePostRequest(subreddit.getId(), post.getTitle(), post.getText())));
  }

  @Test
  void save_ThrowsUserNotFoundException_WhenUserDoesNotExist() {
    // given
    Authentication authentication = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    UserMediator userMediator = new UserMediator(user);
    when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(
        userMediator);

    // when
    assertThrows(UserNotFoundException.class, () ->
        postService.save(new CreatePostRequest(subreddit.getId(), post.getTitle(), post.getText())));
  }

  @Test
  void getAllPosts_ReturnsAllPosts() {
    // given
    given(postRepository.findAll(any(Pageable.class))).willReturn(new PageImpl<>(List.of(post)));
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "title"));

    // when
    Page<PostDto> result = postService.getAllPosts(pageRequest);

    // then
    assertEquals(1, result.getTotalElements());
    assertTrue(result.getContent().contains(modelMapper.map(post, PostDto.class)));
  }

  @Test
  void getPost_ReturnsPost() {
    // given
    given(postRepository.findById(any())).willReturn(Optional.ofNullable(post));

    // when
    PostDto returned = postService.getPost(this.post.getId());

    // then
    assertEquals(post.getTitle(), returned.getTitle());
    assertEquals(post.getText(), returned.getText());
    assertEquals(post.getUser(), returned.getUser());
    assertEquals(post.getSubreddit(), returned.getSubreddit());
  }

  @Test
  void getPost_ThrowsPostNotFoundException_WhenPostDoesNotExist() {
    // given when then
    assertThrows(PostNotFoundException.class, () -> postService.getPost(post.getId()));
  }

  @Test
  void getAllPostsForSubreddit_ReturnsPosts() {
    // given
    given(subredditRepository.findById(any())).willReturn(Optional.ofNullable(subreddit));
    given(postRepository.findAllBySubreddit(any(), any(PageRequest.class)))
        .willReturn(new PageImpl<>(List.of(post)));
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "title"));

    // when
    Page<PostDto> page = postService.getAllPostsForSubreddit(subreddit.getId(), pageRequest);

    // then
    assertEquals(1, page.getTotalElements());
    assertTrue(page.getContent().contains(modelMapper.map(post, PostDto.class)));
  }

  @Test
  void getAllPostsForSubreddit_ThrowsSubredditNotFoundException_WhenSubredditDoesNotExist() {
    // given
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "title"));

    // when then
    assertThrows(SubredditNotFoundException.class, () ->
        postService.getAllPostsForSubreddit(subreddit.getId(), pageRequest));
  }

  @Test
  void getAllPostsForUser_ReturnsPosts() {
    // given
    given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));
    given(postRepository.findAllByUser(any(), any())).willReturn(new PageImpl<>(List.of(post)));
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "title"));

    // when
    Page<PostDto> page = postService.getAllPostsForUser(user.getUsername(), pageRequest);

    // then
    assertEquals(1, page.getTotalElements());
    assertTrue(page.getContent().contains(modelMapper.map(post, PostDto.class)));
  }

  @Test
  void getAllPostsForUser_ThrowsUserNotFoundException_WhenUserDoesNotExist() {
    // given
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "title"));

    // when then
    assertThrows(UserNotFoundException.class, () ->
        postService.getAllPostsForUser(user.getUsername(), pageRequest));
  }

  @Test
  void delete_ReturnsDeletedUser_WhenSuccessfullyDeleted() {
    // given
    given(postRepository.findById(any())).willReturn(Optional.ofNullable(post));

    // when
    PostDto deleted = postService.delete(post.getId());

    // then
    assertNotNull(deleted);
    assertEquals(post.getTitle(), deleted.getTitle());
    assertEquals(post.getText(), deleted.getText());
    assertEquals(post.getUser(), deleted.getUser());
    assertEquals(post.getSubreddit(), deleted.getSubreddit());
  }

  @Test
  void delete_ThrowsPostNotFoundException_WhenPostDoesNotExist() {
    // given when then
    assertThrows(PostNotFoundException.class, () -> postService.delete(post.getId()));
  }

  @Test
  void update_ReturnsUpdatedPost_WhenSuccessfullyUpdated() {
    // given
    given(postRepository.findById(any())).willReturn(Optional.ofNullable(post));
    Post post = new Post(user, "New title", "New text", subreddit);
    given(postRepository.save(any())).willReturn(post);

    // when
    PostDto updated = postService.update(new UpdatePostRequest(this.post.getId(), "New title", "New text"));

    // then
    assertNotNull(updated);
    assertEquals(post.getTitle(), updated.getTitle());
    assertEquals(post.getText(), updated.getText());
  }

  @Test
  void update_ThrowsPostNotFoundException_WhenPostDoesNotExist() {
    // given when then
    assertThrows(PostNotFoundException.class, () ->
        postService.update(new UpdatePostRequest(post.getId(), post.getTitle(), post.getText())));
  }
}