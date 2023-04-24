package com.srecko.subreddit.subredditsservice.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.srecko.reddit.subreddits.assembler.SubredditModelAssembler;
import com.srecko.reddit.subreddits.controller.SubredditController;
import com.srecko.reddit.subreddits.dto.SubredditDto;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class SubredditModelAssemblerTest {

  private SubredditModelAssembler subredditModelAssembler;

  @Mock
  private SubredditController subredditController;

  @BeforeEach
  void setUp() {
    subredditModelAssembler = new SubredditModelAssembler();
  }

  @Test
  void toModel_ReturnsValidEntityModel() {
    // given
    SubredditDto subredditDto = new SubredditDto();
    subredditDto.setId(1L);

    // when
    EntityModel<SubredditDto> result = subredditModelAssembler.toModel(subredditDto);

    // then
    assertNotNull(result);
    assertEquals(subredditDto, result.getContent());
    assertNotNull(result.getLinks());

    Optional<Link> selfLink = result.getLink("self");
    assertNotNull(selfLink);
    assertTrue(selfLink.isPresent());
    assertEquals("/api/subreddits/" + subredditDto.getId(), selfLink.get().getHref());

    Optional<Link> postsLink = result.getLink("posts");
    assertNotNull(postsLink);
    assertTrue(postsLink.isPresent());
    assertEquals("/api/posts/subreddit/" + subredditDto.getId(), postsLink.get().getHref());
  }
}