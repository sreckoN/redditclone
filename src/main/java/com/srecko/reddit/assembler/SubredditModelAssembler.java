package com.srecko.reddit.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.srecko.reddit.controller.SubredditController;
import com.srecko.reddit.entity.Subreddit;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class SubredditModelAssembler implements
    RepresentationModelAssembler<Subreddit, EntityModel<Subreddit>> {

  @Override
  public EntityModel<Subreddit> toModel(Subreddit subreddit) {
    return EntityModel.of(subreddit,
        linkTo(methodOn(SubredditController.class).getSubreddit(subreddit.getId())).withSelfRel(),
        linkTo(methodOn(SubredditController.class).getAll()).withRel("subreddits"));
  }
}