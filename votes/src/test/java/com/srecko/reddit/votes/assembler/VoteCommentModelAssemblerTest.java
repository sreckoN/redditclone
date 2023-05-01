package com.srecko.reddit.votes.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.srecko.reddit.votes.dto.VoteCommentDto;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class VoteCommentModelAssemblerTest {

  private VoteCommentModelAssembler voteCommentModelAssembler;

  @BeforeEach
  void setUp() {
    voteCommentModelAssembler = new VoteCommentModelAssembler();
  }

  @Test
  void toModel_ReturnsValidEntityModel() {
    // given
    VoteCommentDto voteCommentDto = new VoteCommentDto();
    voteCommentDto.setUserId(123L);
    voteCommentDto.setCommentId(123L);

    // when
    EntityModel<VoteCommentDto> result = voteCommentModelAssembler.toModel(voteCommentDto);

    // then
    assertNotNull(result);
    assertEquals(voteCommentDto, result.getContent());
    assertNotNull(result.getLinks());

    /*Optional<Link> commentLink = result.getLink("comment");
    assertNotNull(commentLink);
    assertTrue(commentLink.isPresent());
    assertEquals("/api/comments/" + voteCommentDto.getCommentId(), commentLink.get().getHref());

    Optional<Link> userLink = result.getLink("user");
    assertNotNull(userLink);
    assertTrue(userLink.isPresent());
    assertEquals("/api/users/" + voteCommentDto.getUserId(), userLink.get().getHref());*/
  }
}