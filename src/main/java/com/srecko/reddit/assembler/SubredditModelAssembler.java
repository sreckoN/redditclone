package com.srecko.reddit.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.srecko.reddit.controller.PostController;
import com.srecko.reddit.controller.SubredditController;
import com.srecko.reddit.dto.SubredditDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * The type Subreddit model assembler.
 *
 * @author Srecko Nikolic
 */
@Component
public class SubredditModelAssembler implements
    RepresentationModelAssembler<SubredditDto, EntityModel<SubredditDto>> {

  @Override
  public EntityModel<SubredditDto> toModel(SubredditDto subreddit) {
    return EntityModel.of(subreddit,
        linkTo(methodOn(SubredditController.class).getSubreddit(subreddit.getId())).withSelfRel(),
        linkTo(methodOn(PostController.class).getAllPostsForSubreddit(subreddit.getId(), null, null)).withRel("posts"));
  }
}