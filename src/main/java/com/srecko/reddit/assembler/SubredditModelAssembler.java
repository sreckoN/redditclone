package com.srecko.reddit.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.srecko.reddit.controller.SubredditController;
import com.srecko.reddit.entity.Subreddit;
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
    RepresentationModelAssembler<Subreddit, EntityModel<Subreddit>> {

  @Override
  public EntityModel<Subreddit> toModel(Subreddit subreddit) {
    return EntityModel.of(subreddit,
        linkTo(methodOn(SubredditController.class).getSubreddit(subreddit.getId())).withSelfRel());
  }
}