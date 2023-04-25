package com.srecko.reddit.votes.assembler;

import com.srecko.reddit.votes.dto.VotePostDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * The type Vote post model assembler.
 *
 * @author Srecko Nikolic
 */
@Component
public class VotePostModelAssembler implements
    RepresentationModelAssembler<VotePostDto, EntityModel<VotePostDto>> {

  @Override
  public EntityModel<VotePostDto> toModel(VotePostDto vote) {
    return EntityModel.of(vote);
        /*linkTo(methodOn(PostController.class).getPost(vote.getPost().getId())).withRel("post"),
        linkTo(methodOn(UserController.class).getUser(vote.getUser().getUsername()))
            .withRel("user"));*/
  }
}