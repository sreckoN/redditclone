package com.srecko.reddit.entity;

import java.util.Date;

public class Post {

    private int id;
    private Date dateOfCreation;
    private User createdBy;
    private String title;
    private String text;
    private int votes;
    private int commentsCounter;

    // Comments

    public Post(User createdBy, String title, String text) {
        this.createdBy = createdBy;
        this.title = title;
        this.text = text;
        this.dateOfCreation = new Date(); // check
        this.votes = 0;
        this.commentsCounter = 0;
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
}
