package com.srecko.reddit.assembler;

import static org.junit.jupiter.api.Assertions.*;

import com.srecko.reddit.controller.PostController;
import com.srecko.reddit.controller.SubredditController;
import com.srecko.reddit.dto.SubredditDto;
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

  @Mock
  private PostController postController;

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