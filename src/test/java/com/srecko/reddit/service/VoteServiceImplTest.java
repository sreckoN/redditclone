package com.srecko.reddit.service;

import com.srecko.reddit.dto.VoteDto;
import com.srecko.reddit.entity.*;
import com.srecko.reddit.exception.PostNotFoundException;
import com.srecko.reddit.exception.UserNotFoundException;
import com.srecko.reddit.exception.VoteNotFoundException;
import com.srecko.reddit.repository.PostRepository;
import com.srecko.reddit.repository.UserRepository;
import com.srecko.reddit.repository.VoteRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {VoteServiceImpl.class})
@ExtendWith(SpringExtension.class)
class VoteServiceImplTest {

    @MockBean
    private VoteRepository voteRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PostRepository postRepository;

    @Autowired
    private VoteService voteService;

    private User user;
    private Subreddit subreddit;
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

        comment = new Comment(user, "I can't believe", post);
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        comment.setCreated(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
        comment.setId(123L);
        comment.setVotes(1);
        user.getComments().add(comment);
        post.getComments().add(comment);
    }

    @Test
    void save() {
        // given
        given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(post));

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user.getUsername());

        // when
        Vote saved = voteService.save(new VoteDto(post.getId(), VoteType.UPVOTE));

        // then
        assertNotNull(saved);
        assertEquals(1, post.getVotes());
    }

    @Test
    void saveThrowsPostNotFoundException() {
        // given
        given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user.getUsername());

        // when then
        assertThrows(PostNotFoundException.class, () -> {
            voteService.save(new VoteDto(post.getId(), VoteType.UPVOTE));
        });
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
            voteService.save(new VoteDto(post.getId(), VoteType.UPVOTE));
        });
    }

    @Test
    void delete() {
        // given
        Vote vote = new Vote(post.getUser(), post, VoteType.UPVOTE);
        given(voteRepository.findById(any())).willReturn(Optional.of(vote));
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(post));

        // when
        Vote deleted = voteService.delete(vote.getId());

        // then
        assertNotNull(deleted);
        assertEquals(vote.getUser(), deleted.getUser());
        assertEquals(vote.getPost(), deleted.getPost());
        assertEquals(vote.getType(), deleted.getType());
    }

    @Test
    void deleteThrowsPostNotFoundException() {
        // given
        Vote vote = new Vote(post.getUser(), post, VoteType.UPVOTE);
        given(voteRepository.findById(any())).willReturn(Optional.of(vote));

        // when then
        assertThrows(PostNotFoundException.class, () -> {
            voteService.delete(vote.getId());
        });
    }

    @Test
    void deleteThrowsVoteNotFoundException() {
        // given when then
        assertThrows(VoteNotFoundException.class, () -> {
            voteService.delete(1L);
        });
    }
}