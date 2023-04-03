package com.srecko.reddit.dto.requests;

import com.srecko.reddit.entity.VoteType;
import jakarta.validation.constraints.NotNull;

/**
 * The abstract type Vote request.
 *
 * @author Srecko Nikolic
 */
public abstract class VoteRequest {

  @NotNull
  private VoteType type;

  /**
   * Instantiates a new Vote dto.
   *
   * @param type the type
   */
  public VoteRequest(VoteType type) {
    this.type = type;
  }

  /**
   * Instantiates a new Vote dto.
   */
  public VoteRequest() {
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
