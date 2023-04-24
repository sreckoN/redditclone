package com.srecko.reddit.assembler;

import static org.junit.jupiter.api.Assertions.*;

import com.srecko.reddit.controller.CommentController;
import com.srecko.reddit.controller.PostController;
import com.srecko.reddit.controller.UserController;
import com.srecko.reddit.dto.UserDto;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class UserModelAssemblerTest {

  private UserModelAssembler userModelAssembler;

  @Mock
  private PostController postController;

  @Mock
  private UserController userController;

  @Mock
  private CommentController commentController;

  @BeforeEach
  void setUp() {
    userModelAssembler = new UserModelAssembler();
  }

  @Test
  void toModel_ReturnsValidEntityModel() {
    // given
    UserDto userDto = new UserDto();
    userDto.setUsername("testuser");

    // when
    EntityModel<UserDto> result = userModelAssembler.toModel(userDto);

    // then
    assertNotNull(result);
    assertEquals(userDto, result.getContent());
    assertNotNull(result.getLinks());

    Optional<Link> selfLink = result.getLink("self");
    assertNotNull(selfLink);
    assertTrue(selfLink.isPresent());
    assertEquals("/api/users/" + userDto.getUsername(), selfLink.get().getHref());

    Optional<Link> postsLink = result.getLink("posts");
    assertNotNull(postsLink);
    assertTrue(postsLink.isPresent());
    assertEquals("/api/posts/user/" + userDto.getUsername(), postsLink.get().getHref());

    Optional<Link> commentsLink = result.getLink("comments");
    assertNotNull(commentsLink);
    assertTrue(commentsLink.isPresent());
    assertEquals("/api/comments/user/" + userDto.getUsername(), commentsLink.get().getHref());
  }
}