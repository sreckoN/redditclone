package com.srecko.reddit.entity;

import java.util.Date;

public class Comment {

    private int id;
    private User user;
    private String text;
    private int votes;
    private Date created;
    private Post post;

    public Comment(User user, String text, Post post) {
        this.user = user;
        this.text = text;
        this.post = post;
        this.votes = 0;
        this.created = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}