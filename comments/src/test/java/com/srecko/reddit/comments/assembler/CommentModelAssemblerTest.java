package com.srecko.reddit.comments.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.srecko.reddit.comments.controller.CommentController;
import com.srecko.reddit.comments.dto.CommentDto;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class CommentModelAssemblerTest {

  private CommentModelAssembler commentModelAssembler;

  @Mock
  private CommentController commentController;

  @BeforeEach
  void setUp() {
    commentModelAssembler = new CommentModelAssembler();
  }

  @Test
  void toModel_ReturnsValidEntityModel() {
    // given
    CommentDto commentDto = new CommentDto();
    commentDto.setId(1L);
    commentDto.setPostId(1L);

    // when
    EntityModel<CommentDto> result = commentModelAssembler.toModel(commentDto);

    // then
    assertNotNull(result);
    assertEquals(commentDto, result.getContent());
    assertNotNull(result.getLinks());

    Optional<Link> selfLink = result.getLink("self");
    assertNotNull(selfLink);
    assertTrue(selfLink.isPresent());
    assertEquals("/api/comments/" + commentDto.getId(), selfLink.get().getHref());

   /* Optional<Link> postsLink = result.getLink("all_comments_for_post");
    assertNotNull(postsLink);
    assertTrue(postsLink.isPresent());
    assertEquals("/api/comments/post/" + commentDto.getPostId(), postsLink.get().getHref());*/
  }
}