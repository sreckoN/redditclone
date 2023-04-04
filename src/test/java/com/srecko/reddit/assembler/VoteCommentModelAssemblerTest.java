package com.srecko.reddit.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.srecko.reddit.controller.PostController;
import com.srecko.reddit.controller.UserController;
import com.srecko.reddit.dto.VoteCommentDto;
import com.srecko.reddit.dto.VotePostDto;
import com.srecko.reddit.entity.Comment;
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
class VoteCommentModelAssemblerTest {

  private VoteCommentModelAssembler commentModelAssembler;

  @Mock
  private PostController postController;

  @Mock
  private UserController userController;

  @BeforeEach
  void setUp() {
    commentModelAssembler = new VoteCommentModelAssembler();
  }

  @Test
  void toModel_ReturnsValidEntityModel() {
    // given
    User user = new User();
    user.setUsername("testuser");
    Comment comment = new Comment();
    comment.setId(1L);
    VoteCommentDto voteCommentDto = new VoteCommentDto();
    voteCommentDto.setUser(user);
    voteCommentDto.setComment(comment);

    // when
    EntityModel<VoteCommentDto> result = commentModelAssembler.toModel(voteCommentDto);

    // then
    assertNotNull(result);
    assertEquals(voteCommentDto, result.getContent());
    assertNotNull(result.getLinks());

    Optional<Link> commentLink = result.getLink("comment");
    assertNotNull(commentLink);
    assertTrue(commentLink.isPresent());
    assertEquals("/api/comments/" + comment.getId(), commentLink.get().getHref());

    Optional<Link> userLink = result.getLink("user");
    assertNotNull(userLink);
    assertTrue(userLink.isPresent());
    assertEquals("/api/users/" + user.getUsername(), userLink.get().getHref());
  }
}