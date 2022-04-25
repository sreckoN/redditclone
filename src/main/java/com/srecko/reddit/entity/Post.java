package com.srecko.reddit.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post {

    private int id;
    private Date dateOfCreation;
    private User createdBy;
    private String title;
    private String text;
    private int votes;
    private int commentsCounter;
    private Subreddit subreddit;

    // Comments
    private List<Comment> comments;

    public Post(User createdBy, String title, String text, Subreddit subreddit) {
        this.createdBy = createdBy;
        this.title = title;
        this.text = text;
        this.subreddit = subreddit;
        this.dateOfCreation = new Date(); // check
        this.votes = 0;
        this.commentsCounter = 0;
        this.comments = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
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

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getCommentsCounter() {
        return commentsCounter;
    }

    public void setCommentsCounter(int commentsCounter) {
        this.commentsCounter = commentsCounter;
    }

    public Subreddit getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(Subreddit subreddit) {
        this.subreddit = subreddit;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
