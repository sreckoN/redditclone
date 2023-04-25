package com.srecko.reddit.posts.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.srecko.reddit.posts.controller.PostController;
import com.srecko.reddit.posts.dto.PostDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * The type Post model assembler.
 *
 * @author Srecko Nikolic
 */
@Component
public class PostModelAssembler implements
    RepresentationModelAssembler<PostDto, EntityModel<PostDto>> {

  @Override
  public EntityModel<PostDto> toModel(PostDto post) {
    return EntityModel.of(post,
        linkTo(methodOn(PostController.class).getPost(post.getId())).withSelfRel());
    /*linkTo(methodOn(UserController.class)
        .getUser(post.getUser().getUsername())).withRel("user"),
    linkTo(methodOn(CommentController.class)
        .getCommentsForPost(post.getId(), null, null)).withRel("comments"));*/
  }
}
