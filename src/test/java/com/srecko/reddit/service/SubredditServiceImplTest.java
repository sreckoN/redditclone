package com.srecko.reddit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.srecko.reddit.dto.SubredditDto;
import com.srecko.reddit.dto.UserMediator;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.subreddit.SubredditNotFoundException;
import com.srecko.reddit.exception.user.UserNotFoundException;
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

@ContextConfiguration(classes = {SubredditServiceImpl.class})
@ExtendWith(SpringExtension.class)
class SubredditServiceImplTest {

  @MockBean
  private SubredditRepository subredditRepository;

  @MockBean
  private UserRepository userRepository;

  @Autowired
  private SubredditService subredditService;

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
  void getAll_ReturnsAllSubreddits() {
    // given
    given(subredditRepository.findAll(any(PageRequest.class)))
        .willReturn(new PageImpl<>(List.of(subreddit)));
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

    // when
    Page<Subreddit> page = subredditService.getAll(pageRequest);

    // then
    assertEquals(1, page.getTotalElements());
    assertTrue(page.getContent().contains(subreddit));
  }

  @Test
  void getSubredditById_ReturnsSubreddit() {
    // given
    given(subredditRepository.findById(any())).willReturn(Optional.ofNullable(subreddit));

    // when
    Subreddit subredditById = subredditService.getSubredditById(subreddit.getId());

    // then
    assertNotNull(subredditById);
    assertEquals(subreddit.getName(), subredditById.getName());
    assertEquals(subreddit.getDescription(), subredditById.getDescription());
    assertEquals(subreddit.getCreator(), subredditById.getCreator());
  }

  @Test
  void getSubredditById_ThrowsSubredditNotFoundException_WhenSubredditDoesNotExist() {
    // given when then
    assertThrows(SubredditNotFoundException.class, () -> {
      subredditService.getSubredditById(subreddit.getId());
    });
  }

  @Test
  void save_ReturnsSavedSubreddit_WhenSuccessfullySaved() {
    // given
    given(userRepository.findUserByUsername(any())).willReturn(Optional.of(user));
    given(subredditRepository.save(any())).willReturn(subreddit);

    Authentication authentication = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    UserMediator userMediator = new UserMediator(user);
    when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(
        userMediator);

    // when
    Subreddit saved = subredditService.save(
        new SubredditDto(subreddit.getId(), subreddit.getName(), subreddit.getDescription()));

    // then
    assertNotNull(saved);
    assertEquals(subreddit.getName(), saved.getName());
    assertEquals(subreddit.getDescription(), saved.getDescription());
    assertEquals(subreddit.getCreator(), saved.getCreator());
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

    // when then
    assertThrows(UserNotFoundException.class, () -> {
      subredditService.save(
          new SubredditDto(subreddit.getId(), subreddit.getName(), subreddit.getDescription()));
    });
  }

  @Test
  void delete_ReturnsDeletedSubreddit_WhenSuccessfullyDeleted() {
    // given
    given(subredditRepository.findById(any())).willReturn(Optional.ofNullable(subreddit));

    // when
    Subreddit deleted = subredditService.delete(subreddit.getId());

    // then
    assertEquals(subreddit.getName(), deleted.getName());
    assertEquals(subreddit.getDescription(), deleted.getDescription());
    assertEquals(subreddit.getCreator(), deleted.getCreator());
  }

  @Test
  void delete_ThrowsSubredditNotFoundException_WhenSubredditDoesNotExist() {
    // given when then
    assertThrows(SubredditNotFoundException.class, () -> {
      subredditService.delete(subreddit.getId());
    });
  }

  @Test
  void update_ReturnsUpdatedSubreddit_WhenSuccessfullyUpdated() {
    // given
    given(subredditRepository.findById(any())).willReturn(Optional.ofNullable(subreddit));
    given(subredditRepository.save(any())).willReturn(
        new Subreddit("New name", "New description", user));

    // when
    Subreddit updated = subredditService.update(
        new SubredditDto(subreddit.getId(), "New name", "New description"));

    // then
    assertEquals(subreddit.getName(), updated.getName());
    assertEquals(subreddit.getDescription(), updated.getDescription());
    assertEquals(subreddit.getCreator(), updated.getCreator());
  }

  @Test
  void update_ThrowsSubredditNotFoundException_WhenSubredditDoesNotExist() {
    // given when then
    assertThrows(SubredditNotFoundException.class, () -> {
      subredditService.update(new SubredditDto(subreddit.getId(), "New name", "New description"));
    });
  }
}