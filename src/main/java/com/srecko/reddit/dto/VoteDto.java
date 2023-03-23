package com.srecko.reddit.dto;

import com.srecko.reddit.entity.VoteType;
import jakarta.validation.constraints.NotNull;

public abstract class VoteDto {

  @NotNull
  private VoteType type;

  public VoteDto(VoteType type) {
    this.type = type;
  }

  public VoteDto() {
  }

  public VoteType getType() {
    return type;
  }

  public void setType(VoteType type) {
    this.type = type;
  }
}
