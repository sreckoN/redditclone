package com.srecko.reddit.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.srecko.reddit.controller.PostController;
import com.srecko.reddit.entity.Post;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class PostModelAssembler implements RepresentationModelAssembler<Post, EntityModel<Post>> {

  @Override
  public EntityModel<Post> toModel(Post post) {
    return EntityModel.of(post,
        linkTo(methodOn(PostController.class).getPost(post.getId())).withSelfRel(),
        linkTo(methodOn(PostController.class).getAllPosts()).withRel("posts"));
  }
}
