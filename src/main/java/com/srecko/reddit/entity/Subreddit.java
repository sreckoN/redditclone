package com.srecko.reddit.entity;

import java.util.Date;
import java.util.List;

public class Subreddit {

    private int id;
    private String name;
    private String description;
    private Date createdDate;
    private User creator;
    private List<Post> posts;
    private int numberOfUsers;

    public Subreddit() {
    }

    public Subreddit(String name, String description, User creator) {
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.numberOfUsers = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}