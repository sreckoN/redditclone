package com.srecko.reddit.votes.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.srecko.reddit.votes.dto.VotePostDto;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class VotePostModelAssemblerTest {

  private VotePostModelAssembler postModelAssembler;

  @BeforeEach
  void setUp() {
    postModelAssembler = new VotePostModelAssembler();
  }

  @Test
  void toModel_ReturnsValidEntityModel() {
    // given
    VotePostDto votePostDto = new VotePostDto();
    votePostDto.setUserId(123L);
    votePostDto.setPostId(123L);

    // when
    EntityModel<VotePostDto> result = postModelAssembler.toModel(votePostDto);

    // then
    assertNotNull(result);
    assertEquals(votePostDto, result.getContent());
    assertNotNull(result.getLinks());

    /*Optional<Link> postLink = result.getLink("post");
    assertNotNull(postLink);
    assertTrue(postLink.isPresent());
    assertEquals("/api/posts/" + votePostDto.getPostId(), postLink.get().getHref());

    Optional<Link> userLink = result.getLink("user");
    assertNotNull(userLink);
    assertTrue(userLink.isPresent());
    assertEquals("/api/users/" + votePostDto.getUserId(), userLink.get().getHref());*/
  }
}