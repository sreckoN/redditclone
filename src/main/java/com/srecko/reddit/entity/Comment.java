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
}