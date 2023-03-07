package com.srecko.reddit.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UpdatePostDto {

    @NotNull
    private Long postId;
    @NotEmpty
    private String title;
    @NotEmpty
    private String text;

    public UpdatePostDto(Long id, String title, String text) {
        this.postId = id;
        this.title = title;
        this.text = text;
    }

    public UpdatePostDto() {
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
