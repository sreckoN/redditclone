package com.srecko.reddit.comments.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;

import com.srecko.reddit.comments.dto.CommentDto;
import com.srecko.reddit.comments.dto.CommentRequest;
import com.srecko.reddit.comments.entity.Comment;
import com.srecko.reddit.comments.exception.CommentNotFoundException;
import com.srecko.reddit.comments.repository.CommentRepository;
import com.srecko.reddit.service.utils.TestConfig;
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

@ContextConfiguration(classes = {CommentServiceImpl.class, TestConfig.class})
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class CommentServiceImplTest {

  @MockBean
  private CommentRepository commentRepository;

  @Autowired
  private CommentServiceImpl commentServiceImpl;

  @Autowired
  private final ModelMapper modelMapper = new ModelMapper();

  private Long userId;

  private Long postId;

  private Comment comment;

  @BeforeEach
  void setUp() {
    userId = 1L;
    postId = 1L;
    comment = new Comment(userId, "I can't believe", postId);
    LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
    comment.setCreated(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
    comment.setId(123L);
    comment.setVotes(1);
  }

  @Test
  void testGetAllCommentsForPost_ReturnsComments() {
    // given
    Comment comment2 = new Comment(userId, "Yeah, me neither", postId);
    comment2.setId(222L);
    given(commentRepository.findAllByPostId(any(), any())).willReturn(new PageImpl<>(List.of(comment, comment2)));
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "text"));

    // when
    Page<CommentDto> page = commentServiceImpl.getAllCommentsForPost(postId, pageRequest);

    // then
    assertEquals(2, page.getTotalElements());
    assertTrue(page.getContent().contains(modelMapper.map(comment, CommentDto.class)));
    assertTrue(page.getContent().contains(modelMapper.map(comment2, CommentDto.class)));
  }

  @Test
  void getAllCommentsForUsername_ReturnsComments() {
    // given
    Comment comment2 = new Comment(userId, "Yeah, me neither", postId);
    comment2.setId(222L);
    given(commentRepository.findAllByUserId(any(), any()))
        .willReturn(new PageImpl<>(List.of(comment, comment2)));
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "text"));

    // when
    Page<CommentDto> page = commentServiceImpl.getAllCommentsForUsername(
        "username", pageRequest);

    // then
    assertEquals(2, page.getTotalElements());
    List<CommentDto> content = page.getContent();
    CommentDto map1 = modelMapper.map(comment, CommentDto.class);
    assertTrue(content.contains(map1));
    CommentDto map2 = modelMapper.map(comment2, CommentDto.class);
    assertTrue(content.contains(map2));
  }

  @Test
  void saveComment_ReturnsSavedComment_WhenSuccessfullySaved() {
    // given
    /*Authentication authentication = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    UserMediator userMediator = new UserMediator(user);
    when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(
        userMediator);*/

    // when
    CommentDto savedComment = commentServiceImpl.save(
        new CommentRequest(comment.getText(), postId));

    // then
    assertEquals(comment.getText(), savedComment.getText());
    assertEquals(comment.getUserId(), savedComment.getUserId());
    assertEquals(comment.getPostId(), savedComment.getPostId());
  }

  @Test
  void delete_ReturnsDeletedComment_WhenSuccessfullyDeleted() {
    // given
    given(commentRepository.findById(any())).willReturn(Optional.ofNullable(comment));

    // when
    CommentDto deleted = commentServiceImpl.delete(comment.getId());

    // then
    assertNotNull(deleted);
    assertEquals(comment.getId(), deleted.getId());
    assertEquals(comment.getText(), deleted.getText());
    assertEquals(comment.getUserId(), deleted.getUserId());
    assertEquals(comment.getPostId(), deleted.getPostId());
  }

  @Test
  void delete_ThrowsCommentNotFoundException_WhenCommentNotFound() {
    // given when then
    assertThrows(CommentNotFoundException.class, () -> commentServiceImpl.delete(comment.getId()));
  }
}