package com.srecko.reddit.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CommentDto {

    @NotNull
    private Long id;
    @NotEmpty
    private String text;
    @NotNull
    private Long postId;

    public CommentDto(Long id, String text, Long post) {
        this.id = id;
        this.text = text;
        this.postId = post;
    }

    public CommentDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
