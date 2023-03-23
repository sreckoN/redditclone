package com.srecko.reddit.dto;

import com.srecko.reddit.entity.VoteType;
import jakarta.validation.constraints.NotNull;

/**
 * The abstract type Vote dto.
 *
 * @author Srecko Nikolic
 */
public abstract class VoteDto {

  @NotNull
  private VoteType type;

  /**
   * Instantiates a new Vote dto.
   *
   * @param type the type
   */
  public VoteDto(VoteType type) {
    this.type = type;
  }

  /**
   * Instantiates a new Vote dto.
   */
  public VoteDto() {
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
