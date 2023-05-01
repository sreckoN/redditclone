package com.srecko.reddit.votes.assembler;

import com.srecko.reddit.votes.dto.VoteCommentDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * The type Vote post model assembler.
 *
 * @author Srecko Nikolic
 */
@Component
public class VoteCommentModelAssembler implements
    RepresentationModelAssembler<VoteCommentDto, EntityModel<VoteCommentDto>> {

  @Override
  public EntityModel<VoteCommentDto> toModel(VoteCommentDto vote) {
    return EntityModel.of(vote);
    /*linkTo(methodOn(CommentController.class).getComment(vote.getComment().getId()))
        .withRel("comment"),
    linkTo(methodOn(UserController.class).getUser(vote.getUser().getUsername()))
        .withRel("user"));*/
  }
}