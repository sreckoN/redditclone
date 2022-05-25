package com.srecko.reddit.dto;

import com.srecko.reddit.entity.VoteType;

import javax.validation.constraints.NotNull;

public class VoteDto {

    @NotNull
    private Long postId;
    @NotNull
    private VoteType type;

    public VoteDto(Long postId, VoteType type) {
        this.postId = postId;
        this.type = type;
    }

    public VoteDto() {
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public VoteType getType() {
        return type;
    }

    public void setType(VoteType type) {
        this.type = type;
    }
}
