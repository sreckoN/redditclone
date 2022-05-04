package com.srecko.reddit.dto;

import com.srecko.reddit.entity.Post;
import com.srecko.reddit.entity.User;

public class CommentDto {

    private Long id;
    private String text;
    private Post post;

    public CommentDto(Long id, String text, Post post) {
        this.id = id;
        this.text = text;
        this.post = post;
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
