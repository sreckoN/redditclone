package com.srecko.reddit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.srecko.reddit.dto.CommentDto;
import com.srecko.reddit.entity.Comment;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.Subreddit;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.exception.CommentNotFoundException;
import com.srecko.reddit.exception.PostNotFoundException;
import com.srecko.reddit.exception.UserNotFoundException;
import com.srecko.reddit.repository.CommentRepository;
import com.srecko.reddit.repository.PostRepository;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CommentServiceImpl.class})
@ExtendWith(SpringExtension.class)
class CommentServiceImplTest {

    @MockBean
    private CommentRepository commentRepository;

    @Autowired
    private CommentServiceImpl commentServiceImpl;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private UserRepository userRepository;

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
    void testGetAllCommentsForPost() {
        // given
        Comment comment2 = new Comment(user, "Yeah, me neither", post);
        post.getComments().add(comment2);
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(post));
        given(commentRepository.findAllByPost(post)).willReturn(post.getComments());

        // when
        List<Comment> commentsForPost = commentServiceImpl.getAllCommentsForPost(post.getId());

        // then
        assertEquals(2, commentsForPost.size());
        assertTrue(commentsForPost.contains(comment));
        assertTrue(commentsForPost.contains(comment2));
    }

    @Test
    void testGetAllCommentsForPostThrowsPostNotFoundException() {
        // given
        assertThrows(PostNotFoundException.class, () -> {
            commentServiceImpl.getAllCommentsForPost(post.getId());
        });
    }

    @Test
    void getAllCommentsForUsername() {
        // given
        Comment comment2 = new Comment(user, "Yeah, me neither", post);
        user.getComments().add(comment2);
        given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));
        given(commentRepository.findAllByUser(user)).willReturn(user.getComments());

        // when
        List<Comment> commentsForPost = commentServiceImpl.getAllCommentsForUsername(user.getUsername());

        // then
        assertEquals(2, commentsForPost.size());
        assertTrue(commentsForPost.contains(comment));
        assertTrue(commentsForPost.contains(comment2));
    }

    @Test
    void getAllCommentsForUsernameThrowsUserNotFoundException() {
        // given when then
        assertThrows(UserNotFoundException.class, () -> {
            commentServiceImpl.getAllCommentsForUsername(user.getUsername());
        });
    }

    @Test
    void saveComment() {
        // given
        given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(post));

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user.getUsername());

        // when
        Comment savedComment = commentServiceImpl.save(new CommentDto(comment.getText(), comment.getPost().getId()));

        // then
        assertEquals(comment.getText(), savedComment.getText());
        assertEquals(comment.getUser(), savedComment.getUser());
        assertEquals(comment.getPost(), savedComment.getPost());
    }

    @Test
    void saveCommentThrowsUserNotFoundException() {
        // given
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user.getUsername());

        // when then
        assertThrows(UserNotFoundException.class, () -> {
            commentServiceImpl.save(new CommentDto(comment.getText(), comment.getPost().getId()));
        });
    }

    @Test
    void saveCommentThrowsPostNotFoundException() {
        // given
        given(userRepository.findUserByUsername(any())).willReturn(Optional.ofNullable(user));

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user.getUsername());

        // when then
        assertThrows(PostNotFoundException.class, () -> {
            commentServiceImpl.save(new CommentDto(comment.getText(), comment.getPost().getId()));
        });
    }

    @Test
    void delete() {
        // given
        given(commentRepository.findById(any())).willReturn(Optional.ofNullable(comment));

        // when
        Comment deleted = commentServiceImpl.delete(comment.getId());

        // then
        assertEquals(comment, deleted);
    }

    @Test
    void deleteThrowsCommentNotFoundException() {
        // given when then
        assertThrows(CommentNotFoundException.class, () -> {
            commentServiceImpl.delete(comment.getId());
        });
    }
}