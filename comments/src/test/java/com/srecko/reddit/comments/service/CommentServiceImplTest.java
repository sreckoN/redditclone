package com.srecko.reddit.comments.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;

import com.srecko.reddit.comments.dto.CommentDto;
import com.srecko.reddit.comments.dto.CommentRequest;
import com.srecko.reddit.comments.entity.Comment;
import com.srecko.reddit.comments.entity.CommentParentType;
import com.srecko.reddit.comments.exception.CommentNotFoundException;
import com.srecko.reddit.comments.repository.CommentRepository;
import com.srecko.reddit.comments.service.client.PostsFeignClient;
import com.srecko.reddit.comments.service.client.UsersFeignClient;
import com.srecko.reddit.comments.service.utils.TestConfig;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CommentServiceImpl.class, TestConfig.class})
@ExtendWith(SpringExtension.class)
@ActiveProfiles("dev")
class CommentServiceImplTest {

  @MockBean
  private CommentRepository commentRepository;

  @Autowired
  private CommentServiceImpl commentService;

  @MockBean
  private UsersFeignClient usersFeignClient;

  @MockBean
  private PostsFeignClient postsFeignClient;

  @Autowired
  private final ModelMapper modelMapper = new ModelMapper();

  private Long userId;

  private Long postId;

  private Comment comment;

  @BeforeEach
  void setUp() {
    userId = 1L;
    postId = 1L;
    comment = new Comment(userId, "I can't believe", CommentParentType.POST, postId);
    comment.setId(123L);
    comment.setVotes(1);
    LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
    comment.setCreated(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
  }

  @Test
  void getAllCommentsForPost_ReturnsComments() {
    // given
    Comment comment2 = new Comment(userId, "Yeah, me neither", CommentParentType.POST, postId);
    comment2.setId(222L);
    doNothing().when(postsFeignClient).checkIfPostExists(any());
    given(commentRepository.findAllByParentTypeAndParentId(any(), any(), any())).willReturn(new PageImpl<>(List.of(comment, comment2)));
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "text"));

    // when
    Page<CommentDto> page = commentService.getAllCommentsForPost(postId, pageRequest);

    // then
    assertEquals(2, page.getTotalElements());
    assertTrue(page.getContent().contains(modelMapper.map(comment, CommentDto.class)));
    assertTrue(page.getContent().contains(modelMapper.map(comment2, CommentDto.class)));
  }

  @Test
  void getAllCommentsForComment_ReturnsComments() {
    // given
    Comment comment2 = new Comment(userId, "Yeah, me neither", CommentParentType.COMMENT, comment.getId());
    comment2.setId(222L);
    given(commentRepository.findById(any())).willReturn(Optional.of(comment2));
    given(commentRepository.findAllByParentTypeAndParentId(any(), any(), any())).willReturn(new PageImpl<>(List.of(comment2)));
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "text"));

    // when
    Page<CommentDto> page = commentService.getAllCommentsForComment(comment.getId(), pageRequest);

    // then
    assertEquals(1, page.getTotalElements());
    assertTrue(page.getContent().contains(modelMapper.map(comment2, CommentDto.class)));
  }

  @Test
  void getAllCommentsForUser_ReturnsComments() {
    // given
    Comment comment2 = new Comment(userId, "Yeah, me neither", CommentParentType.POST, postId);
    comment2.setId(222L);
    doNothing().when(usersFeignClient).checkIfExists(any());
    given(commentRepository.findAllByUserId(any(), any()))
        .willReturn(new PageImpl<>(List.of(comment, comment2)));
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "text"));

    // when
    Page<CommentDto> page = commentService.getAllCommentsForUser(userId, pageRequest);

    // then
    assertEquals(2, page.getTotalElements());
    List<CommentDto> content = page.getContent();
    CommentDto map1 = modelMapper.map(comment, CommentDto.class);
    assertTrue(content.contains(map1));
    CommentDto map2 = modelMapper.map(comment2, CommentDto.class);
    assertTrue(content.contains(map2));
  }

  @Test
  void save_ReturnsSavedCommentForPost_WhenSuccessfullySaved() {
    // given
    /*Authentication authentication = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    UserMediator userMediator = new UserMediator(user);
    when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(
        userMediator);*/
    doNothing().when(postsFeignClient).checkIfPostExists(any());
    given(commentRepository.save(any())).willReturn(comment);
    doNothing().when(postsFeignClient).increaseCommentCounter(any());

    // when
    CommentDto savedComment = commentService.save(
        new CommentRequest(comment.getText(), CommentParentType.POST, postId));

    // then
    assertEquals(comment.getText(), savedComment.getText());
    assertEquals(comment.getUserId(), savedComment.getUserId());
    assertEquals(comment.getParentId(), savedComment.getParentId());
    assertEquals(comment.getParentType(), savedComment.getParentType());
  }

  @Test
  void save_ReturnsSavedCommentForComment_WhenSuccessfullySaved() {
    // given
    /*Authentication authentication = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    UserMediator userMediator = new UserMediator(user);
    when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(
        userMediator);*/
    Comment comment1 = new Comment(userId, "Hello", CommentParentType.COMMENT, comment.getId());
    given(commentRepository.findById(any())).willReturn(Optional.of(comment1));
    given(commentRepository.save(any())).willReturn(comment1);

    // when
    CommentDto savedComment = commentService.save(
        new CommentRequest(comment1.getText(), comment1.getParentType(), comment1.getParentId()));

    // then
    assertEquals(comment1.getText(), savedComment.getText());
    assertEquals(comment1.getUserId(), savedComment.getUserId());
    assertEquals(comment1.getParentId(), savedComment.getParentId());
    assertEquals(comment1.getParentType(), savedComment.getParentType());
  }

  @Test
  void delete_ReturnsDeletedCommentForPost_WhenSuccessfullyDeleted() {
    // given
    given(commentRepository.findById(any())).willReturn(Optional.ofNullable(comment));
    doNothing().when(commentRepository).delete(any());
    doNothing().when(postsFeignClient).decreaseCommentCounter(any());

    // when
    CommentDto deleted = commentService.delete(comment.getId());

    // then
    assertNotNull(deleted);
    assertEquals(comment.getId(), deleted.getId());
    assertEquals(comment.getText(), deleted.getText());
    assertEquals(comment.getUserId(), deleted.getUserId());
    assertEquals(comment.getParentId(), deleted.getParentId());
  }

  @Test
  void delete_ReturnsDeletedCommentForComment_WhenSuccessfullyDeleted() {
    // given
    Comment comment1 = new Comment(userId, "Hello", CommentParentType.COMMENT, comment.getId());
    comment1.setId(222L);
    given(commentRepository.findById(any())).willReturn(Optional.ofNullable(comment1));

    // when
    CommentDto deleted = commentService.delete(comment1.getId());

    // then
    assertNotNull(deleted);
    assertEquals(comment1.getId(), deleted.getId());
    assertEquals(comment1.getText(), deleted.getText());
    assertEquals(comment1.getUserId(), deleted.getUserId());
    assertEquals(comment1.getParentId(), deleted.getParentId());
  }

  @Test
  void delete_ThrowsCommentNotFoundException_WhenCommentNotFound() {
    // given when then
    assertThrows(CommentNotFoundException.class, () -> commentService.delete(comment.getId()));
  }

  @Test
  void getComment_ReturnsCommentDto_WhenFound() {
    // given
    given(commentRepository.findById(any())).willReturn(Optional.ofNullable(comment));

    // when
    CommentDto actual = commentService.getComment(this.comment.getId());

    // then
    assertNotNull(actual);
    assertEquals(comment.getId(), actual.getId());
    assertEquals(comment.getUserId(), actual.getUserId());
    assertEquals(comment.getParentId(), actual.getParentId());
  }

  @Test
  void getComment_ThrowsCommentNotFoundException_WhenNotFound() {
    // given when then
    assertThrows(CommentNotFoundException.class, () -> {
      commentService.getComment(comment.getId());
    });
  }

  @Test
  void getAllComments_ReturnsPageOfCommentDtos() {
    // given
    Comment comment1 = new Comment(userId, "Yeah, me neither", CommentParentType.POST, postId);
    comment1.setId(222L);
    Pageable pageable = PageRequest.of(1, 1, Sort.by(Direction.ASC, "text"));
    given(commentRepository.findAll(any(PageRequest.class))).willReturn(
        new PageImpl<>(List.of(comment, comment1))
    );

    // when
    Page<CommentDto> actual = commentService.getAllComments(pageable);

    // then
    assertNotNull(actual);
    assertEquals(2, actual.getTotalElements());
    assertEquals(2, actual.getTotalPages());
    List<CommentDto> content = actual.getContent();
    assertTrue(content.contains(modelMapper.map(comment, CommentDto.class)));
    assertTrue(content.contains(modelMapper.map(comment1, CommentDto.class)));
  }

  @Test
  void checkIfExists_NothingHappens_WhenCommentExists() {
    // given
    given(commentRepository.findById(any())).willReturn(Optional.ofNullable(comment));

    // when then
    assertDoesNotThrow(() -> commentService.checkIfExists(comment.getId()));
  }

  @Test
  void checkIfExists_ThrowsCommentNotFoundException_WhenCommentDoesNotExist() {
    // given
    given(commentRepository.findById(any())).willReturn(Optional.empty());

    // when then
    assertThrows(CommentNotFoundException.class, () -> commentService.checkIfExists(comment.getId()));
  }

  @Test
  void search_ReturnsPageOfCommentDtos_WhenTheyMatchQuery() {
    // given
    String query = "believe";
    Pageable pageable = PageRequest.of(1, 10, Sort.by(Direction.ASC, "text"));
    Comment comment1 = new Comment(userId, "Do you believe?", CommentParentType.POST, postId);
    comment1.setId(124L);

    given(commentRepository.findByTextContainingIgnoreCase(any(), any())).willReturn(
        new PageImpl<>(List.of(comment, comment1))
    );

    // when
    Page<CommentDto> actual = commentService.search(query, pageable);

    // then
    assertNotNull(actual);
    List<CommentDto> content = actual.getContent();
    assertEquals(2, content.size());
    assertTrue(content.contains(modelMapper.map(comment, CommentDto.class)));
    assertTrue(content.contains(modelMapper.map(comment1, CommentDto.class)));
  }

  @Test
  void increaseCommentCounter_IncreasesCommentCounter() {
    // given
    int before = comment.getCommentsCounter();
    given(commentRepository.findById(any())).willReturn(Optional.ofNullable(comment));
    given(commentRepository.save(any())).willReturn(null);

    // when
    commentService.increaseCommentCounter(comment.getId());

    // then
    assertEquals(before + 1, comment.getCommentsCounter());
  }

  @Test
  void decreaseCommentCounter_DecreasesCommentCounter() {
    // given
    int before = comment.getCommentsCounter();
    given(commentRepository.findById(any())).willReturn(Optional.ofNullable(comment));
    given(commentRepository.save(any())).willReturn(null);

    // when
    commentService.decreaseCommentCounter(comment.getId());

    // then
    assertEquals(before - 1, comment.getCommentsCounter());
  }

  @Test
  void decreaseCommentCounter_ThrowsCommentNotFoundException_WhenCommentNotFound() {
    // given
    given(commentRepository.findById(any())).willReturn(Optional.empty());

    // when then
    assertThrows(CommentNotFoundException.class, () -> {
      commentService.decreaseCommentCounter(comment.getId());
    });
  }
}