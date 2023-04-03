package com.srecko.reddit.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.User;
import com.srecko.reddit.entity.VoteType;

/**
 * The type Vote post dto.
 *
 * @author Srecko Nikolic
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class VotePostDto {

  private Long id;

  @JsonIdentityReference(alwaysAsId = true)
  private User user;

  @JsonIdentityReference(alwaysAsId = true)
  private Post post;

  private VoteType type;

  /**
   * Instantiates a new Vote post dto.
   */
  public VotePostDto() {
  }

  /**
   * Gets id.
   *
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets id.
   *
   * @param id the id
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Gets user.
   *
   * @return the user
   */
  public User getUser() {
    return user;
  }

  /**
   * Sets user.
   *
   * @param user the user
   */
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Gets post.
   *
   * @return the post
   */
  public Post getPost() {
    return post;
  }

  /**
   * Sets post.
   *
   * @param post the post
   */
  public void setPost(Post post) {
    this.post = post;
  }

  /**
   * Gets type.
   *
   * @return the type
   */
  public VoteType getType() {
    return type;
  }

  /**
   * Sets type.
   *
   * @param type the type
   */
  public void setType(VoteType type) {
    this.type = type;
  }
}
