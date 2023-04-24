package com.srecko.subreddit.subredditsservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.srecko.reddit.service.utils.TestConfig;
import com.srecko.reddit.subreddits.dto.SubredditDto;
import com.srecko.reddit.subreddits.dto.SubredditRequest;
import com.srecko.reddit.subreddits.entity.Subreddit;
import com.srecko.reddit.subreddits.exception.SubredditNotFoundException;
import com.srecko.reddit.subreddits.repository.SubredditRepository;
import com.srecko.reddit.subreddits.service.SubredditService;
import com.srecko.reddit.subreddits.service.SubredditServiceImpl;
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
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {SubredditServiceImpl.class, TestConfig.class})
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class SubredditServiceImplTest {

  @MockBean
  private SubredditRepository subredditRepository;

  @Autowired
  private SubredditService subredditService;

  private Long userId;

  private Subreddit subreddit;

  private final ModelMapper modelMapper = new ModelMapper();

  @BeforeEach
  void setUp() {
    userId = 123L;
    subreddit = new Subreddit("Name", "The characteristics of someone or something", userId);
    LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
    subreddit.setCreatedDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));
    subreddit.setId(123L);
  }

  @Test
  void getAll_ReturnsAllSubreddits() {
    // given
    given(subredditRepository.findAll(any(PageRequest.class)))
        .willReturn(new PageImpl<>(List.of(subreddit)));
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));

    // when
    Page<SubredditDto> page = subredditService.getAll(pageRequest);

    // then
    assertEquals(1, page.getTotalElements());
    assertTrue(page.getContent().contains(modelMapper.map(subreddit, SubredditDto.class)));
  }

  @Test
  void getSubredditById_ReturnsSubreddit() {
    // given
    given(subredditRepository.findById(any())).willReturn(Optional.ofNullable(subreddit));

    // when
    SubredditDto subredditById = subredditService.getSubredditById(subreddit.getId());

    // then
    assertNotNull(subredditById);
    assertEquals(subreddit.getName(), subredditById.getName());
    assertEquals(subreddit.getDescription(), subredditById.getDescription());
    assertEquals(subreddit.getCreatorId(), subredditById.getCreatorId());
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
    given(subredditRepository.save(any())).willReturn(subreddit);

    /*Authentication authentication = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    UserMediator userMediator = new UserMediator(user);
    when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(
        userMediator);*/

    // when
    SubredditDto saved = subredditService.save(
        new SubredditRequest(subreddit.getId(), subreddit.getName(), subreddit.getDescription()));

    // then
    assertNotNull(saved);
    assertEquals(subreddit.getName(), saved.getName());
    assertEquals(subreddit.getDescription(), saved.getDescription());
    assertEquals(subreddit.getCreatorId(), saved.getCreatorId());
  }

  @Test
  void delete_ReturnsDeletedSubreddit_WhenSuccessfullyDeleted() {
    // given
    given(subredditRepository.findById(any())).willReturn(Optional.ofNullable(subreddit));

    // when
    SubredditDto deleted = subredditService.delete(subreddit.getId());

    // then
    assertEquals(subreddit.getName(), deleted.getName());
    assertEquals(subreddit.getDescription(), deleted.getDescription());
    assertEquals(subreddit.getCreatorId(), deleted.getCreatorId());
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
        new Subreddit("New name", "New description", userId));

    // when
    SubredditDto updated = subredditService.update(
        new SubredditRequest(subreddit.getId(), "New name", "New description"));

    // then
    assertEquals(subreddit.getName(), updated.getName());
    assertEquals(subreddit.getDescription(), updated.getDescription());
    assertEquals(subreddit.getCreatorId(), updated.getCreatorId());
  }

  @Test
  void update_ThrowsSubredditNotFoundException_WhenSubredditDoesNotExist() {
    // given when then
    assertThrows(SubredditNotFoundException.class, () -> {
      subredditService.update(new SubredditRequest(subreddit.getId(), "New name", "New description"));
    });
  }
}