package com.srecko.reddit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.srecko.reddit.entity.Comment;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.repository.CommentRepository;
import com.srecko.reddit.repository.PostRepository;
import com.srecko.reddit.repository.SubredditRepository;
import com.srecko.reddit.repository.UserRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {SearchServiceImpl.class})
@ExtendWith(SpringExtension.class)
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
    given(postRepository.findByTitleContainingIgnoreCase(any(), any())).willReturn(
        new PageImpl<>(List.of(post)));

    // when
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "title"));
    Page<Post> result = searchService.searchPosts("Serbia", pageRequest);

    // then
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    assertEquals(1, result.getTotalPages());
  }

  @Test
  void searchPosts_ReturnsPageOfPosts_WithDefaultSortWhenWrongSortGiven() {
    // given
    Post post1 = new Post(user, "Serbia's best!", "Check out the best in Serbia", subreddit);
    Post post2 = new Post(user, "What's good in Serbia", "Everything is good in Serbia", subreddit);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.DATE, -7);
    post2.setDateOfCreation(calendar.getTime());
    given(postRepository.findByTitleContainingIgnoreCase(any(), any())).willReturn(
        new PageImpl<>(List.of(post1, post2)));

    // when
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
    Page<Post> result = searchService.searchPosts("Serbia", pageRequest);

    // then
    assertNotNull(result);
    assertEquals(2, result.getTotalElements());
    assertEquals(1, result.getTotalPages());

    List<Post> content = result.getContent();
    assertEquals(post1, content.get(0));
    assertEquals(post2, content.get(1));
  }

  @Test
  void searchComments_ReturnsPageOfComments() {
    // given
    Post post = new Post(user, "Serbia's best!", "Check out the best in Serbia", subreddit);
    Comment comment = new Comment(user, "What's the best place in Serbia?", post);
    given(commentRepository.findByTextContainingIgnoreCase(any(), any())).willReturn(
        new PageImpl<>(List.of(comment)));

    // when
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "text"));
    Page<Comment> result = searchService.searchComments("Serbia", pageRequest);

    // then
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    assertEquals(1, result.getTotalPages());
  }

  @Test
  void searchComments_ReturnsPageOfComments_WithDefaultSortWhenWrongSortGiven() {
    // given
    Post post = new Post(user, "Serbia's best!", "Check out the best in Serbia", subreddit);
    Comment comment1 = new Comment(user, "What's the best place in Serbia?", post);
    Comment comment2 = new Comment(user, "I love Serbia", post);
    given(commentRepository.findByTextContainingIgnoreCase(any(), any())).willReturn(
        new PageImpl<>(List.of(comment1, comment2)));

    // when
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
    Page<Comment> result = searchService.searchComments("Serbia", pageRequest);

    // then
    assertNotNull(result);
    assertEquals(2, result.getTotalElements());
    assertEquals(1, result.getTotalPages());

    List<Comment> content = result.getContent();
    assertEquals(comment1, content.get(0));
    assertEquals(comment2, content.get(1));
  }

  @Test
  void searchUsers_ReturnsPageOfUsers() {
    // given
    given(userRepository.findByUsernameContainingIgnoreCase(any(), any())).willReturn(
        new PageImpl<>(List.of(user)));

    // when
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "username"));
    Page<User> result = searchService.searchUsers("jane", pageRequest);

    // then
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    assertEquals(1, result.getTotalPages());
  }

  @Test
  void searchUsers_ReturnsPageOfUsers_WithDefaultSortWhenWrongSortGiven() {
    // given
    User user2 = new User("Jane", "Smith", "jane.smith@example.org", "jane.smith", "iloveyou", "GB",
        true);
    given(userRepository.findByUsernameContainingIgnoreCase(any(), any())).willReturn(
        new PageImpl<>(List.of(user, user2)));

    // when
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
    Page<User> result = searchService.searchUsers("jane", pageRequest);

    // then
    assertNotNull(result);
    assertEquals(2, result.getTotalElements());
    assertEquals(1, result.getTotalPages());

    List<User> content = result.getContent();
    assertEquals(content.size(), 2);
  }

  @Test
  void searchSubreddits_ReturnsPageOfSubreddits() {
    // given
    given(subredditRepository.findByNameContainingIgnoreCase(any(), any())).willReturn(
        new PageImpl<>(List.of(subreddit)));

    // when
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
    Page<Subreddit> result = searchService.searchSubreddits("Serbia", pageRequest);

    // then
    assertNotNull(result);
    assertEquals(1, result.getTotalElements());
    assertEquals(1, result.getTotalPages());
  }

  @Test
  void searchSubreddits_ReturnsPageOfSubreddits_WithDefaultSortWhenWrongSortGiven() {
    // given
    Subreddit subreddit2 = new Subreddit("Programming Serbia", "Serbian programmers unite!", user);
    given(subredditRepository.findByNameContainingIgnoreCase(any(), any())).willReturn(
        new PageImpl<>(List.of(subreddit, subreddit2)));

    // when
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
    Page<Subreddit> result = searchService.searchSubreddits("Serbia", pageRequest);

    // then
    assertNotNull(result);
    assertEquals(2, result.getTotalElements());
    assertEquals(1, result.getTotalPages());

    List<Subreddit> content = result.getContent();
    assertEquals(subreddit, content.get(0));
    assertEquals(subreddit2, content.get(1));
  }
}