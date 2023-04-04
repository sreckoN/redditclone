package com.srecko.reddit.assembler;

import static org.junit.jupiter.api.Assertions.*;

import com.srecko.reddit.controller.CommentController;
import com.srecko.reddit.dto.CommentDto;
import com.srecko.reddit.entity.Post;
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
    Post post = new Post();
    post.setId(1L);
    commentDto.setPost(post);

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

    Optional<Link> postsLink = result.getLink("all_comments_for_post");
    assertNotNull(postsLink);
    assertTrue(postsLink.isPresent());
    assertEquals("/api/comments/post/" + post.getId(), postsLink.get().getHref());
  }
}