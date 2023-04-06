package com.srecko.reddit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.srecko.reddit.dto.CommentDto;
import com.srecko.reddit.dto.PostDto;
import com.srecko.reddit.dto.SubredditDto;
import com.srecko.reddit.dto.UserDto;
import com.srecko.reddit.entity.Comment;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.subreddit.SubredditNotFoundException;
import com.srecko.reddit.repository.CommentRepository;
import com.srecko.reddit.repository.PostRepository;
import com.srecko.reddit.repository.SubredditRepository;
import com.srecko.reddit.repository.UserRepository;
import com.srecko.reddit.service.utils.TestConfig;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {SearchServiceImpl.class, TestConfig.class})
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class SearchServiceImplTest {

  @MockBean
  private PostRepository postRepository;

  @MockBean
  private CommentRepository commentRepository;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private SubredditRepository subredditRepository;

  @Autowired
  private SearchService searchService;

  private User user;

  private Subreddit subreddit;

  private final ModelMapper modelMapper = new ModelMapper();

  @BeforeEach
  void setUp() {
    user = new User("Jane", "Doe", "jane.doe@example.org", "janedoe", "iloveyou", "GB", true);
    user.setId(123L);
    user.setComments(new ArrayList<>());
    user.setPosts(new ArrayList<>());
    LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
    user.setRegistrationDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));

    subreddit = new Subreddit("Serbia", "Serbia's official subreddit", user);
    LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
    subreddit.setCreatedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
    subreddit.setId(123L);
    subreddit.setPosts(new ArrayList<>());
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void searchPosts_ReturnsPageOfPosts() {
    // given
    Post post = new Post(user, "Serbia's best!", "Check out the best in Serbia", subreddit);
    post.setId(111L);
    given(postRepository.findByTitleContainingIgnoreCase(any(), any())).willReturn(
        new PageImpl<>(List.of(post)));

    // when
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "title"));
    Page<PostDto> result = searchService.searchPosts("Serbia", pageRequest);

    // then
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    assertEquals(1, result.getTotalPages());
    assertTrue(result.getContent().contains(modelMapper.map(post, PostDto.class)));
  }

  @Test
  void searchPosts_ReturnsPageOfPosts_WithDefaultSortWhenWrongSortGiven() {
    // given
    Post post1 = new Post(user, "Serbia's best!", "Check out the best in Serbia", subreddit);
    post1.setId(111L);
    Post post2 = new Post(user, "What's good in Serbia", "Everything is good in Serbia", subreddit);
    post2.setId(222L);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.DATE, -7);
    post2.setDateOfCreation(calendar.getTime());
    given(postRepository.findByTitleContainingIgnoreCase(any(), any())).willReturn(
        new PageImpl<>(List.of(post1, post2)));

    // when
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
    Page<PostDto> result = searchService.searchPosts("Serbia", pageRequest);

    // then
    assertNotNull(result);
    assertEquals(2, result.getTotalElements());
    assertEquals(1, result.getTotalPages());

    List<PostDto> content = result.getContent();
    assertEquals(modelMapper.map(post1, PostDto.class), content.get(0));
    assertEquals(modelMapper.map(post2, PostDto.class), content.get(1));
  }

  @Test
  void searchComments_ReturnsPageOfComments() {
    // given
    Post post = new Post(user, "Serbia's best!", "Check out the best in Serbia", subreddit);
    Comment comment = new Comment(user, "What's the best place in Serbia?", post);
    comment.setId(111L);
    given(commentRepository.findByTextContainingIgnoreCase(any(), any())).willReturn(
        new PageImpl<>(List.of(comment)));

    // when
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "text"));
    Page<CommentDto> result = searchService.searchComments("Serbia", pageRequest);

    // then
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    assertEquals(1, result.getTotalPages());
    assertTrue(result.getContent().contains(modelMapper.map(comment, CommentDto.class)));
  }

  @Test
  void searchComments_ReturnsPageOfComments_WithDefaultSortWhenWrongSortGiven() {
    // given
    Post post = new Post(user, "Serbia's best!", "Check out the best in Serbia", subreddit);
    Comment comment1 = new Comment(user, "What's the best place in Serbia?", post);
    comment1.setId(111L);
    Comment comment2 = new Comment(user, "I love Serbia", post);
    comment2.setId(222L);
    given(commentRepository.findByTextContainingIgnoreCase(any(), any())).willReturn(
        new PageImpl<>(List.of(comment1, comment2)));

    // when
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
    Page<CommentDto> result = searchService.searchComments("Serbia", pageRequest);

    // then
    assertNotNull(result);
    assertEquals(2, result.getTotalElements());
    assertEquals(1, result.getTotalPages());

    List<CommentDto> content = result.getContent();
    assertEquals(modelMapper.map(comment1, CommentDto.class), content.get(0));
    assertEquals(modelMapper.map(comment2, CommentDto.class), content.get(1));
  }

  @Test
  void searchUsers_ReturnsPageOfUsers() {
    // given
    given(userRepository.findByUsernameContainingIgnoreCase(any(), any())).willReturn(
        new PageImpl<>(List.of(user)));

    // when
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "username"));
    Page<UserDto> result = searchService.searchUsers("jane", pageRequest);

    // then
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    assertEquals(1, result.getTotalPages());
    assertTrue(result.getContent().contains(modelMapper.map(user, UserDto.class)));
  }

  @Test
  void searchUsers_ReturnsPageOfUsers_WithDefaultSortWhenWrongSortGiven() {
    // given
    user.setId(111L);
    User user2 = new User("Jane", "Smith", "jane.smith@example.org", "jane.smith", "iloveyou", "GB",
        true);
    user2.setId(222L);
    given(userRepository.findByUsernameContainingIgnoreCase(any(), any())).willReturn(
        new PageImpl<>(List.of(user, user2)));

    // when
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
    Page<UserDto> result = searchService.searchUsers("jane", pageRequest);

    // then
    assertNotNull(result);
    assertEquals(2, result.getTotalElements());
    assertEquals(1, result.getTotalPages());

    List<UserDto> content = result.getContent();
    assertEquals(content.size(), 2);
    assertTrue(content.contains(modelMapper.map(user, UserDto.class)));
    assertTrue(content.contains(modelMapper.map(user2, UserDto.class)));
  }

  @Test
  void searchSubreddits_ReturnsPageOfSubreddits() {
    // given
    given(subredditRepository.findByNameContainingIgnoreCase(any(), any())).willReturn(
        new PageImpl<>(List.of(subreddit)));

    // when
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
    Page<SubredditDto> result = searchService.searchSubreddits("Serbia", pageRequest);

    // then
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    assertEquals(1, result.getTotalPages());
    assertTrue(result.getContent().contains(modelMapper.map(subreddit, SubredditDto.class)));
  }

  @Test
  void searchSubreddits_ReturnsPageOfSubreddits_WithDefaultSortWhenWrongSortGiven() {
    // given
    subreddit.setId(111L);
    Subreddit subreddit2 = new Subreddit("Programming Serbia", "Serbian programmers unite!", user);
    subreddit2.setId(222L);
    given(subredditRepository.findByNameContainingIgnoreCase(any(), any())).willReturn(
        new PageImpl<>(List.of(subreddit, subreddit2)));

    // when
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
    Page<SubredditDto> result = searchService.searchSubreddits("Serbia", pageRequest);

    // then
    assertNotNull(result);
    assertEquals(2, result.getTotalElements());
    assertEquals(1, result.getTotalPages());

    List<SubredditDto> content = result.getContent();
    assertEquals(modelMapper.map(subreddit, SubredditDto.class), content.get(0));
    assertEquals(modelMapper.map(subreddit2, SubredditDto.class), content.get(1));
  }

  @Test
  void searchPostsInSubreddit_ReturnsPageOfPostsFromSubreddit() {
    // given
    Post post1 = new Post(user, "How is the weather in Serbia?", "Is it warm?", subreddit);
    post1.setId(111L);
    post1.setDateOfCreation(new Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000));
    Post post2 = new Post(user, "Serbia's player just won", "Congrats", subreddit);
    post2.setId(222L);
    given(subredditRepository.existsById(any())).willReturn(true);
    given(postRepository.findBySubredditIdAndTitleContainingIgnoreCase(any(), any(), any())).willReturn(
        new PageImpl<>(List.of(post1, post2)));

    // when
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "title"));
    Page<PostDto> result = searchService.searchPostsInSubreddit(subreddit.getId(), "Serbia", pageRequest);

    // then
    assertNotNull(result);
    assertEquals(2, result.getTotalElements());
    assertEquals(1, result.getTotalPages());
    assertTrue(result.getContent().contains(modelMapper.map(post1, PostDto.class)));
    assertTrue(result.getContent().contains(modelMapper.map(post2, PostDto.class)));
  }

  @Test
  void searchPostsInSubreddit_ReturnsPageOfPosts_WithDefaultSortWhenWrongSortGiven() {
    // given
    Post post1 = new Post(user, "Serbia's best!", "Check out the best in Serbia", subreddit);
    post1.setId(111L);
    Post post2 = new Post(user, "What's good in Serbia", "Everything is good in Serbia", subreddit);
    post2.setId(222L);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.DATE, -7);
    post2.setDateOfCreation(calendar.getTime());
    given(subredditRepository.existsById(any())).willReturn(true);
    given(postRepository.findBySubredditIdAndTitleContainingIgnoreCase(any(), any(), any())).willReturn(
        new PageImpl<>(List.of(post1, post2)));

    // when
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
    Page<PostDto> result = searchService.searchPostsInSubreddit(subreddit.getId(), "Serbia", pageRequest);

    // then
    assertNotNull(result);
    assertEquals(2, result.getTotalElements());
    assertEquals(1, result.getTotalPages());

    List<PostDto> content = result.getContent();
    assertEquals(modelMapper.map(post1, PostDto.class), content.get(0));
    assertEquals(modelMapper.map(post2, PostDto.class), content.get(1));
  }

  @Test
  void searchPostsInSubreddit_ThrowsSubredditNotFoundException_WhenSubredditNotFound() {
    // given
    given(subredditRepository.existsById(any())).willReturn(false);

    // when then
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
    assertThrows(SubredditNotFoundException.class, () -> {
      searchService.searchPostsInSubreddit(0L, "Serbia", pageRequest);
    });
  }
}