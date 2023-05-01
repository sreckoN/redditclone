package com.srecko.reddit.search.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.srecko.reddit.search.dto.CommentDto;
import com.srecko.reddit.search.dto.PostDto;
import com.srecko.reddit.search.dto.SubredditDto;
import com.srecko.reddit.search.dto.UserDto;
import com.srecko.reddit.search.service.client.CommentsFeignClient;
import com.srecko.reddit.search.service.client.PostsFeignClient;
import com.srecko.reddit.search.service.client.SubredditsFeignClient;
import com.srecko.reddit.search.service.client.UsersFeignClient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {SearchServiceImpl.class})
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class SearchServiceImplTest {

  @MockBean
  private UsersFeignClient usersFeignClient;

  @MockBean
  private SubredditsFeignClient subredditsFeignClient;

  @MockBean
  private PostsFeignClient postsFeignClient;

  @MockBean
  private CommentsFeignClient commentsFeignClient;

  @Autowired
  private SearchService searchService;

  @Test
  void searchUsers_ReturnsPageOfUsers() {
    // given
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "username"));

    UserDto userDto = new UserDto();
    userDto.setId(123L);
    userDto.setUsername("janedoe");
    userDto.setCountry("GB");
    LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
    userDto.setRegistrationDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));

    given(usersFeignClient.searchUsers(any(), any())).willReturn(
        PagedModel.of(Collections.singletonList(EntityModel.of(userDto)), new PageMetadata(1, 0, 1)));

    // when
    PagedModel<EntityModel<UserDto>> actual = searchService.searchUsers("jane", pageRequest);

    // then
    assertNotNull(actual);
    assertEquals(1, actual.getMetadata().getTotalElements());
    assertEquals(1, actual.getMetadata().getTotalPages());
    assertTrue(actual.getContent().contains(EntityModel.of(userDto)));
  }

  @Test
  void searchUsers_ReturnsPageOfUsersWithDefaultSort_WhenWrongSortGiven() {
    // given
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

    UserDto userDto = new UserDto();
    userDto.setId(123L);
    userDto.setUsername("janedoe");
    userDto.setCountry("GB");
    LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
    userDto.setRegistrationDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));

    UserDto userDto2 = new UserDto();
    userDto2.setId(124L);
    userDto2.setUsername("janesmith");

    given(usersFeignClient.searchUsers(any(), any())).willReturn(
        PagedModel.of(Arrays.asList(EntityModel.of(userDto), EntityModel.of(userDto2)), new PageMetadata(2, 0, 2)));

    // when
    PagedModel<EntityModel<UserDto>> actual = searchService.searchUsers("jane", pageRequest);

    // then
    assertNotNull(actual);
    assertEquals(2, actual.getMetadata().getTotalElements());
    assertEquals(1, actual.getMetadata().getTotalPages());

    List<EntityModel<UserDto>> actualContent = new ArrayList<>(actual.getContent());
    assertEquals(actualContent.size(), 2);
    assertEquals(EntityModel.of(userDto), actualContent.get(0));
    assertEquals(EntityModel.of(userDto2), actualContent.get(1));
  }

  @Test
  void searchPosts_ReturnsPageOfPosts() {
    // given
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "title"));
    PostDto post = new PostDto();
    post.setId(111L);
    post.setTitle("Serbia's best!");
    post.setText("Check out the best in Serbia");

    given(postsFeignClient.searchPosts(any(), any())).willReturn(
        PagedModel.of(Collections.singletonList(EntityModel.of(post)), new PageMetadata(1, 0, 1)));

    // when
    PagedModel<EntityModel<PostDto>> actual = searchService.searchPosts("Serbia", pageRequest);

    // then
    assertNotNull(actual);
    assertEquals(1, actual.getMetadata().getTotalElements());
    assertEquals(1, actual.getMetadata().getTotalPages());
    assertTrue(actual.getContent().contains(EntityModel.of(post)));
  }

  @Test
  void searchPosts_ReturnsPageOfPostsWithDefaultSort_WhenWrongSortGiven() {
    // given
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

    PostDto post = new PostDto();
    post.setId(111L);
    post.setTitle("Serbia's best!");
    post.setText("Check out the best in Serbia");

    PostDto post2 = new PostDto();
    post2.setId(222L);
    post2.setTitle("What's good in Serbia");
    post2.setText("Everything is good in Serbia");
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.DATE, -7);
    post2.setDateOfCreation(calendar.getTime());

    given(postsFeignClient.searchPosts(any(), any())).willReturn(
        PagedModel.of(Arrays.asList(EntityModel.of(post), EntityModel.of(post2)), new PageMetadata(2, 0, 2)));

    // when
    PagedModel<EntityModel<PostDto>> actual = searchService.searchPosts("Serbia", pageRequest);

    // then
    assertNotNull(actual);
    assertEquals(2, actual.getMetadata().getTotalElements());
    assertEquals(1, actual.getMetadata().getTotalPages());

    List<EntityModel<PostDto>> actualContent = new ArrayList<>(actual.getContent());
    assertEquals(actualContent.size(), 2);
    assertEquals(EntityModel.of(post), actualContent.get(0));
    assertEquals(EntityModel.of(post2), actualContent.get(1));
  }

  @Test
  void searchComments_ReturnsPageOfComments() {
    // given
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "text"));

    CommentDto comment = new CommentDto();
    comment.setId(111L);
    comment.setText("What's the best place in Serbia?");

    given(commentsFeignClient.searchComments(any(), any())).willReturn(
        PagedModel.of(Collections.singletonList(EntityModel.of(comment)), new PageMetadata(1, 0, 1))
    );

    // when
    PagedModel<EntityModel<CommentDto>> actual = searchService.searchComments("Serbia", pageRequest);

    // then
    assertNotNull(actual);
    assertEquals(1, actual.getMetadata().getTotalElements());
    assertEquals(1, actual.getMetadata().getTotalPages());
    assertTrue(actual.getContent().contains(EntityModel.of(comment)));
  }

  @Test
  void searchComments_ReturnsPageOfComments_WithDefaultSortWhenWrongSortGiven() {
    // given
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

    CommentDto comment1 = new CommentDto();
    comment1.setId(111L);
    comment1.setText("What's the best place in Serbia?");
    CommentDto comment2 = new CommentDto();
    comment2.setId(222L);
    comment2.setText("I love Serbia");

    given(commentsFeignClient.searchComments(any(), any())).willReturn(
        PagedModel.of(Arrays.asList(EntityModel.of(comment1), EntityModel.of(comment2)), new PageMetadata(2, 0, 2)));

    // when
    PagedModel<EntityModel<CommentDto>> actual = searchService.searchComments("Serbia", pageRequest);

    // then
    assertNotNull(actual);
    assertEquals(2, actual.getMetadata().getTotalElements());
    assertEquals(1, actual.getMetadata().getTotalPages());

    List<EntityModel<CommentDto>> actualContent = new ArrayList<>(actual.getContent());
    assertEquals(actualContent.size(), 2);
    assertEquals(EntityModel.of(comment1), actualContent.get(0));
    assertEquals(EntityModel.of(comment2), actualContent.get(1));
  }

  @Test
  void searchSubreddits_ReturnsPageOfSubreddits() {
    // given
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));

    SubredditDto subredditDto = new SubredditDto();
    subredditDto.setId(123L);
    subredditDto.setName("serbia");

    given(subredditsFeignClient.searchSubreddits(any(), any())).willReturn(
        PagedModel.of(Collections.singletonList(EntityModel.of(subredditDto)), new PageMetadata(1, 0, 1)));

    // when
    PagedModel<EntityModel<SubredditDto>> actual = searchService.searchSubreddits("Serbia", pageRequest);

    // then
    assertNotNull(actual);
    assertEquals(1, actual.getMetadata().getTotalElements());
    assertEquals(1, actual.getMetadata().getTotalPages());
    assertTrue(actual.getContent().contains(EntityModel.of(subredditDto)));
  }

  @Test
  void searchSubreddits_ReturnsPageOfSubredditsWithDefaultSort_WhenWrongSortGiven() {
    // given
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

    SubredditDto subredditDto = new SubredditDto();
    subredditDto.setId(123L);
    subredditDto.setName("serbia");
    LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
    subredditDto.setCreatedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

    SubredditDto subredditDto2 = new SubredditDto();
    subredditDto2.setId(222L);
    subredditDto2.setName("Programming Serbia");

    given(subredditsFeignClient.searchSubreddits(any(), any())).willReturn(
        PagedModel.of(Arrays.asList(EntityModel.of(subredditDto), EntityModel.of(subredditDto2)), new PageMetadata(2, 0, 2)));

    // when
    PagedModel<EntityModel<SubredditDto>> actual = searchService.searchSubreddits("Serbia", pageRequest);

    // then
    assertNotNull(actual);
    assertEquals(2, actual.getMetadata().getTotalElements());
    assertEquals(1, actual.getMetadata().getTotalPages());

    List<EntityModel<SubredditDto>> actualContent = new ArrayList<>(actual.getContent());
    assertEquals(actualContent.size(), 2);
    assertEquals(EntityModel.of(subredditDto), actualContent.get(0));
    assertEquals(EntityModel.of(subredditDto2), actualContent.get(1));
  }

  @Test
  void searchPostsInSubreddit_ReturnsPageOfPostsFromSubreddit() {
    // given
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "title"));

    Long subredditId = 1L;

    PostDto postDto = new PostDto();
    postDto.setId(111L);
    postDto.setTitle("How is the weather in Serbia?");
    postDto.setText("Is it warm?");
    postDto.setSubredditId(subredditId);
    postDto.setDateOfCreation(new Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000));

    PostDto postDto2 = new PostDto();
    postDto2.setId(222L);
    postDto2.setTitle("Serbia's player just won");
    postDto2.setText("Congrats");
    postDto2.setSubredditId(subredditId);

    given(postsFeignClient.searchPostsInSubreddit(any(), any(), any())).willReturn(
        PagedModel.of(Arrays.asList(EntityModel.of(postDto), EntityModel.of(postDto2)), new PageMetadata(2, 0, 2)));

    // when
    PagedModel<EntityModel<PostDto>> actual = searchService.searchPostsInSubreddit(subredditId, "Serbia", pageRequest);

    // then
    assertNotNull(actual);
    assertEquals(2, actual.getMetadata().getTotalElements());
    assertEquals(1, actual.getMetadata().getTotalPages());
    assertTrue(actual.getContent().contains(EntityModel.of(postDto)));
    assertTrue(actual.getContent().contains(EntityModel.of(postDto2)));
  }

  @Test
  void searchPostsInSubreddit_ReturnsPageOfPostsWithDefaultSort_WhenWrongSortGiven() {
    // given
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

    Long subredditId = 1L;

    PostDto postDto = new PostDto();
    postDto.setId(111L);
    postDto.setTitle("How is the weather in Serbia?");
    postDto.setText("Is it warm?");
    postDto.setSubredditId(subredditId);
    postDto.setDateOfCreation(new Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000));

    PostDto postDto2 = new PostDto();
    postDto2.setId(222L);
    postDto2.setTitle("Serbia's player just won");
    postDto2.setText("Congrats");
    postDto2.setSubredditId(subredditId);

    given(postsFeignClient.searchPostsInSubreddit(any(), any(), any())).willReturn(
        PagedModel.of(Arrays.asList(EntityModel.of(postDto), EntityModel.of(postDto2)), new PageMetadata(2, 0, 2)));

    // when
    PagedModel<EntityModel<PostDto>> actual = searchService.searchPostsInSubreddit(subredditId, "Serbia", pageRequest);

    // then
    assertNotNull(actual);
    assertEquals(2, actual.getMetadata().getTotalElements());
    assertEquals(1, actual.getMetadata().getTotalPages());

    List<EntityModel<PostDto>> actualContent = new ArrayList<>(actual.getContent());
    assertEquals(actualContent.size(), 2);
    assertEquals(EntityModel.of(postDto), actualContent.get(0));
    assertEquals(EntityModel.of(postDto2), actualContent.get(1));
  }
}