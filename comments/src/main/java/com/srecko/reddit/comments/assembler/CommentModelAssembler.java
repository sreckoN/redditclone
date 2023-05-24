package com.srecko.reddit.comments.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.srecko.reddit.comments.controller.CommentController;
import com.srecko.reddit.comments.dto.CommentDto;
import com.srecko.reddit.comments.entity.CommentParentType;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * The type Comment model assembler.
 *
 * @author Srecko Nikolic
 */
@Component
public class CommentModelAssembler implements
    RepresentationModelAssembler<CommentDto, EntityModel<CommentDto>> {

  @Override
  public EntityModel<CommentDto> toModel(CommentDto comment) {
    Link selfRel = linkTo(methodOn(CommentController.class).getComment(comment.getId()))
        .withSelfRel();
    Link getAllLink;
    if (comment.getParentType().equals(CommentParentType.POST)) {
      getAllLink = linkTo(methodOn(CommentController.class)
          .getCommentsForPost(comment.getParentId(), null, null))
          .withRel("all_comments_for_post");
    } else {
      getAllLink = linkTo(methodOn(CommentController.class)
          .getCommentsForComment(comment.getParentId(), null, null))
          .withRel("all_comments_for_comment");
    }
    return EntityModel.of(comment, selfRel, getAllLink);
  }
}