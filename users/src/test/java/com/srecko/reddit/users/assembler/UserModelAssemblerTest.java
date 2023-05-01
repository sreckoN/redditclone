package com.srecko.reddit.users.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.srecko.reddit.users.controller.UserController;
import com.srecko.reddit.users.dto.UserDto;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class UserModelAssemblerTest {

  private UserModelAssembler userModelAssembler;

  @Mock
  private UserController userController;

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