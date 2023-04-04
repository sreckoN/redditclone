package com.srecko.reddit.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.srecko.reddit.controller.CommentController;
import com.srecko.reddit.controller.PostController;
import com.srecko.reddit.controller.UserController;
import com.srecko.reddit.dto.UserDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * The type User model assembler.
 *
 * @author Srecko Nikolic
 */
@Component
public class UserModelAssembler implements
    RepresentationModelAssembler<UserDto, EntityModel<UserDto>> {

  @Override
  public EntityModel<UserDto> toModel(UserDto user) {
    return EntityModel.of(user,
        linkTo(methodOn(UserController.class).getUser(user.getUsername())).withSelfRel(),
        linkTo(methodOn(PostController.class).getPostsForUser(user.getUsername(), null, null)).withRel("posts"),
        linkTo(methodOn(CommentController.class).getCommentsForUsername(user.getUsername(), null, null)).withRel("comments"));
  }
}
