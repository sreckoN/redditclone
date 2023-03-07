package com.srecko.reddit.dto;

import com.srecko.reddit.entity.VoteType;
import jakarta.validation.constraints.NotNull;

public class VotePostDto extends VoteDto {

    @NotNull
    private Long postId;

    public VotePostDto(VoteType type, Long postId) {
        super(type);
        this.postId = postId;
    }

    public VotePostDto() {
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
