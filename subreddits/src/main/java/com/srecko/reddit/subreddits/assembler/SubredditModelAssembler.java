package com.srecko.reddit.subreddits.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.srecko.reddit.subreddits.controller.SubredditController;
import com.srecko.reddit.subreddits.dto.SubredditDto;
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
        linkTo(methodOn(SubredditController.class).getSubreddit(subreddit.getId())).withSelfRel());
  }
}