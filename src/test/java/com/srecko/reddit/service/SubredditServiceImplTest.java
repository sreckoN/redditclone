package com.srecko.reddit.service;

import com.srecko.reddit.dto.SubredditDto;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.SubredditNotFoundException;
import com.srecko.reddit.exception.UserNotFoundException;
import com.srecko.reddit.repository.SubredditRepository;
import com.srecko.reddit.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

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
    void getAll() {
        // given
        given(subredditRepository.findAll()).willReturn(List.of(subreddit));

        // when
        List<Subreddit> all = subredditService.getAll();

        // then
        assertEquals(1, all.size());
        assertTrue(all.contains(subreddit));
    }

    @Test
    void getSubredditById() {
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
    void getSubredditByIdThrowsSubredditNotFoundException() {
        // given when then
        assertThrows(SubredditNotFoundException.class, () -> {
            subredditService.getSubredditById(subreddit.getId());
        });
    }

    @Test
    void save() {
        // given
        given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user.getUsername());

        // when
        Subreddit saved = subredditService.save(new SubredditDto(subreddit.getId(), subreddit.getName(), subreddit.getDescription()));

        // then
        assertNotNull(saved);
        assertEquals(subreddit.getName(), saved.getName());
        assertEquals(subreddit.getDescription(), saved.getDescription());
        assertEquals(subreddit.getCreator(), saved.getCreator());
    }

    @Test
    void saveThrowsUserNotFoundException() {
        // given
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user.getUsername());

        // when then
        assertThrows(UserNotFoundException.class, () -> {
            subredditService.save(new SubredditDto(subreddit.getId(), subreddit.getName(), subreddit.getDescription()));
        });
    }

    @Test
    void delete() {
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
    void deleteThrowsSubredditNotFoundException() {
        // given when then
        assertThrows(SubredditNotFoundException.class, () -> {
            subredditService.delete(subreddit.getId());
        });
    }

    @Test
    void update() {
        // given
        given(subredditRepository.findById(any())).willReturn(Optional.ofNullable(subreddit));
        given(subredditRepository.save(any())).willReturn(new Subreddit("New name", "New description", user));

        // when
        Subreddit updated = subredditService.update(new SubredditDto(subreddit.getId(), "New name", "New description"));

        // then
        assertEquals(subreddit.getName(), updated.getName());
        assertEquals(subreddit.getDescription(), updated.getDescription());
        assertEquals(subreddit.getCreator(), updated.getCreator());
    }

    @Test
    void updateThrowsSubredditNotFoundException() {
        // given when then
        assertThrows(SubredditNotFoundException.class, () -> {
            subredditService.update(new SubredditDto(subreddit.getId(), "New name", "New description"));
        });
    }
}