package com.srecko.reddit.assembler;

import static org.junit.jupiter.api.Assertions.*;

import com.srecko.reddit.controller.PostController;
import com.srecko.reddit.controller.UserController;
import com.srecko.reddit.dto.VotePostDto;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class VotePostModelAssemblerTest {

  private VotePostModelAssembler postModelAssembler;

  @Mock
  private PostController postController;

  @Mock
  private UserController userController;

  @BeforeEach
  void setUp() {
    postModelAssembler = new VotePostModelAssembler();
  }

  @Test
  void toModel_ReturnsValidEntityModel() {
    // given
    User user = new User();
    user.setUsername("testuser");
    Post post = new Post();
    post.setId(1L);
    VotePostDto votePostDto = new VotePostDto();
    votePostDto.setUser(user);
    votePostDto.setPost(post);

    // when
    EntityModel<VotePostDto> result = postModelAssembler.toModel(votePostDto);

    // then
    assertNotNull(result);
    assertEquals(votePostDto, result.getContent());
    assertNotNull(result.getLinks());

    Optional<Link> postLink = result.getLink("post");
    assertNotNull(postLink);
    assertTrue(postLink.isPresent());
    assertEquals("/api/posts/" + post.getId(), postLink.get().getHref());

    Optional<Link> userLink = result.getLink("user");
    assertNotNull(userLink);
    assertTrue(userLink.isPresent());
    assertEquals("/api/users/" + user.getUsername(), userLink.get().getHref());
  }
}