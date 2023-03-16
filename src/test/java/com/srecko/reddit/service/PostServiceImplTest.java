package com.srecko.reddit.service;

import com.srecko.reddit.dto.CreatePostDto;
import com.srecko.reddit.dto.UpdatePostDto;
import com.srecko.reddit.dto.UserMediator;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.PostNotFoundException;
import com.srecko.reddit.exception.SubredditNotFoundException;
import com.srecko.reddit.exception.UserNotFoundException;
import com.srecko.reddit.repository.PostRepository;
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

@ContextConfiguration(classes = {PostServiceImpl.class})
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
    void save() {
        // given
        given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));
        given(subredditRepository.findById(any())).willReturn(Optional.ofNullable(subreddit));

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        UserMediator userMediator = new UserMediator(user);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userMediator);

        // when
        Post saved = postService.save(new CreatePostDto(subreddit.getId(), post.getTitle(), post.getText()));

        // then
        assertNotNull(saved);
        assertEquals(post.getTitle(), saved.getTitle());
        assertEquals(post.getText(), saved.getText());
        assertEquals(post.getUser(), saved.getUser());
        assertEquals(post.getSubreddit(), saved.getSubreddit());
    }

    @Test
    void saveThrowsSubredditNotFoundException() {
        // given
        given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        UserMediator userMediator = new UserMediator(user);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userMediator);

        // when
        assertThrows(SubredditNotFoundException.class, () -> {
            postService.save(new CreatePostDto(subreddit.getId(), post.getTitle(), post.getText()));
        });
    }

    @Test
    void saveThrowsUserNotFoundException() {
        // given
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        UserMediator userMediator = new UserMediator(user);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userMediator);

        // when
        assertThrows(UserNotFoundException.class, () -> {
            postService.save(new CreatePostDto(subreddit.getId(), post.getTitle(), post.getText()));
        });
    }

    @Test
    void getAllPosts() {
        // given
        given(postRepository.findAll()).willReturn(List.of(post));

        // when
        List<Post> allPosts = postService.getAllPosts();

        // then
        assertEquals(1, allPosts.size());
    }

    @Test
    void getPost() {
        // given
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(post));

        // when
        Post returned = postService.getPost(this.post.getId());

        // then
        assertEquals(post.getTitle(), returned.getTitle());
        assertEquals(post.getText(), returned.getText());
        assertEquals(post.getUser(), returned.getUser());
        assertEquals(post.getSubreddit(), returned.getSubreddit());
    }

    @Test
    void getPostThrowsPostNotFoundException() {
        // given when then
        assertThrows(PostNotFoundException.class, () -> {
            postService.getPost(post.getId());
        });
    }

    @Test
    void getAllPostsForSubreddit() {
        // given
        given(subredditRepository.findById(any())).willReturn(Optional.ofNullable(subreddit));
        given(postRepository.findAllBySubreddit(any())).willReturn(List.of(post));

        // when
        List<Post> allPostsForSubreddit = postService.getAllPostsForSubreddit(subreddit.getId());

        // then
        assertEquals(1, allPostsForSubreddit.size());
        assertTrue(allPostsForSubreddit.contains(post));
    }

    @Test
    void getAllPostsForSubredditThrowsSubredditNotFoundException() {
        // given when then
        assertThrows(SubredditNotFoundException.class, () -> {
            postService.getAllPostsForSubreddit(subreddit.getId());
        });
    }

    @Test
    void getAllPostsForUser() {
        // given
        given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));
        given(postRepository.findAllByUser(any())).willReturn(List.of(post));

        // when
        List<Post> allPostsForUser = postService.getAllPostsForUser(user.getUsername());

        // then
        assertEquals(1, allPostsForUser.size());
        assertTrue(allPostsForUser.contains(post));
    }

    @Test
    void getAllPostsForUserThrowsUserNotFoundException() {
        // given when then
        assertThrows(UserNotFoundException.class, () -> {
            postService.getAllPostsForUser(user.getUsername());
        });
    }

    @Test
    void delete() {
        // given
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(post));

        // when
        Post deleted = postService.delete(post.getId());

        // then
        assertNotNull(deleted);
        assertEquals(post.getTitle(), deleted.getTitle());
        assertEquals(post.getText(), deleted.getText());
        assertEquals(post.getUser(), deleted.getUser());
        assertEquals(post.getSubreddit(), deleted.getSubreddit());
    }

    @Test
    void deleteThrowsPostNotFoundException() {
        // given when then
        assertThrows(PostNotFoundException.class, () -> {
            postService.delete(post.getId());
        });
    }

    @Test
    void update() {
        // given
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(post));
        given(postRepository.save(any())).willReturn(new Post(user, "New title", "New text", subreddit));

        // when
        Post updated = postService.update(new UpdatePostDto(post.getId(), "New title", "New text"));

        // then
        assertNotNull(updated);
        assertEquals("New title", updated.getTitle());
        assertEquals("New text", updated.getText());
    }

    @Test
    void updateThrowsPostNotFoundException() {
        // given when then
        assertThrows(PostNotFoundException.class, () -> {
            postService.update(new UpdatePostDto(post.getId(), post.getTitle(), post.getText()));
        });
    }
}