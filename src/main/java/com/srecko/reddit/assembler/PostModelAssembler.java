package com.srecko.reddit.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.srecko.reddit.controller.PostController;
import com.srecko.reddit.dto.PostDto;
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
  }
}
