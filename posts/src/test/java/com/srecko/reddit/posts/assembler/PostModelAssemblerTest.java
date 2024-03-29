package com.srecko.reddit.posts.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.srecko.reddit.posts.controller.PostController;
import com.srecko.reddit.posts.dto.PostDto;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class PostModelAssemblerTest {

  private PostModelAssembler postModelAssembler;

  @Mock
  private PostController postController;

  @BeforeEach
  void setUp() {
    postModelAssembler = new PostModelAssembler();
  }

  @Test
  void toModel_ReturnsValidEntityModel() {
    // given
    PostDto postDto = new PostDto();
    postDto.setId(1L);
    postDto.setTitle("Test post");
    postDto.setText("This is a test post");
    postDto.setUserId(123L);

    // when
    EntityModel<PostDto> result = postModelAssembler.toModel(postDto);

    // then
    assertNotNull(result);
    assertEquals(postDto, result.getContent());
    assertNotNull(result.getLinks());

    Optional<Link> selfLink = result.getLink("self");
    assertNotNull(selfLink);
    assertTrue(selfLink.isPresent());
    assertEquals("/api/posts/" + postDto.getId(), selfLink.get().getHref());

    /*Optional<Link> userLink = result.getLink("user");
    assertNotNull(userLink);
    assertTrue(userLink.isPresent());
    assertEquals("/api/users/" + postDto.getUserId(), userLink.get().getHref());

    Optional<Link> commentsLink = result.getLink("comments");
    assertNotNull(commentsLink);
    assertTrue(commentsLink.isPresent());
    assertEquals("/api/comments/post/" + postDto.getId(), commentsLink.get().getHref());*/
  }
}