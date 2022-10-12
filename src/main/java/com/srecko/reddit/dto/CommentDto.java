package com.srecko.reddit.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CommentDto {

    @NotEmpty
    private String text;
    @NotNull
    private Long postId;

    public CommentDto(String text, Long post) {
        this.text = text;
        this.postId = post;
    }

    public CommentDto() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
