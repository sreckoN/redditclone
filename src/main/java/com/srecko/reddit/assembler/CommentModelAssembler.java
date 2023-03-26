package com.srecko.reddit.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.srecko.reddit.controller.CommentController;
import com.srecko.reddit.entity.Comment;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * The type Comment model assembler.
 *
 * @author Srecko Nikolic
 */
@Component
public class CommentModelAssembler implements
    RepresentationModelAssembler<Comment, EntityModel<Comment>> {

  @Override
  public EntityModel<Comment> toModel(Comment comment) {
    return EntityModel.of(comment,
        linkTo(methodOn(CommentController.class).getComment(comment.getId())).withSelfRel(),
        linkTo(methodOn(CommentController.class).getAllComments()).withRel("comments"));
  }
}