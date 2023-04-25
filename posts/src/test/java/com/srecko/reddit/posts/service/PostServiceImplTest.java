package com.srecko.reddit.posts.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.srecko.reddit.posts.dto.CreatePostRequest;
import com.srecko.reddit.posts.dto.PostDto;
import com.srecko.reddit.posts.dto.UpdatePostRequest;
import com.srecko.reddit.posts.entity.Post;
import com.srecko.reddit.posts.exception.PostNotFoundException;
import com.srecko.reddit.posts.repository.PostRepository;
import com.srecko.reddit.posts.service.client.SubredditsFeignClient;
import com.srecko.reddit.posts.service.client.UsersFeignClient;
import com.srecko.reddit.posts.service.utils.TestConfig;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {PostServiceImpl.class, TestConfig.class})
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class PostServiceImplTest {

  @MockBean
  private PostRepository postRepository;

  @MockBean
  private UsersFeignClient usersFeignClient;

  @MockBean
  private SubredditsFeignClient subredditsFeignClient;

  @Autowired
  private PostService postService;

  private final ModelMapper modelMapper = new ModelMapper();

  private Long userId;
  private Long subredditId;
  private Post post;

  @BeforeEach
  void setUp() {
    userId = 123L;
    subredditId = 123L;

    post = new Post(userId, "New Post", "This is a new post", subredditId);
    post.setCommentsCounter(1);
    LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
    post.setDateOfCreation(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
    post.setId(123L);
  }

  @Test
  void save_ReturnsSavedPost_WhenSuccessfullySaved() {
    // given
    /*given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));
    given(subredditRepository.findById(any())).willReturn(Optional.ofNullable(subreddit));

    Authentication authentication = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    UserMediator userMediator = new UserMediator(user);
    when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(
        userMediator);*/

    // when
    PostDto saved = postService.save(
        new CreatePostRequest(subredditId, post.getTitle(), post.getText()));

    // then
    assertNotNull(saved);
    assertEquals(post.getTitle(), saved.getTitle());
    assertEquals(post.getText(), saved.getText());
    assertEquals(post.getUserId(), saved.getUserId());
    assertEquals(post.getSubredditId(), saved.getSubredditId());
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
    assertEquals(post.getUserId(), returned.getUserId());
    assertEquals(post.getSubredditId(), returned.getSubredditId());
  }

  @Test
  void getPost_ThrowsPostNotFoundException_WhenPostDoesNotExist() {
    // given when then
    assertThrows(PostNotFoundException.class, () -> postService.getPost(post.getId()));
  }

  @Test
  void getAllPostsForSubreddit_ReturnsPosts() {
    // given
    /*given(subredditRepository.findById(any())).willReturn(Optional.ofNullable(subreddit));
    given(postRepository.findAllBySubreddit(any(), any(PageRequest.class)))
        .willReturn(new PageImpl<>(List.of(post)));*/
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "title"));

    // when
    Page<PostDto> page = postService.getAllPostsForSubreddit(subredditId, pageRequest);

    // then
    assertEquals(1, page.getTotalElements());
    assertTrue(page.getContent().contains(modelMapper.map(post, PostDto.class)));
  }

  @Test
  void getAllPostsForUser_ReturnsPosts() {
    // given
    /*given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));
    given(postRepository.findAllByUser(any(), any())).willReturn(new PageImpl<>(List.of(post)));*/
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "title"));

    // when
    Page<PostDto> page = postService.getAllPostsForUser("username", pageRequest);

    // then
    assertEquals(1, page.getTotalElements());
    assertTrue(page.getContent().contains(modelMapper.map(post, PostDto.class)));
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
    assertEquals(post.getUserId(), deleted.getUserId());
    assertEquals(post.getSubredditId(), deleted.getSubredditId());
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
    Post post = new Post(userId, "New title", "New text", subredditId);
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