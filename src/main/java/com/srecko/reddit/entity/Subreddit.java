package com.srecko.reddit.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "subreddits")
public class Subreddit {

    @Id
    @GeneratedValue
    private Long id;
    @NotEmpty
    @Size(min = 3, max = 50)
    private String name;
    private String description;
    private Date createdDate;
    private int numberOfUsers;
    @NotEmpty
    @OneToOne
    @JoinColumn(name = "creator_id")
    private User creator;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "subreddit", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    public Subreddit() {
    }

    public Subreddit(String name, String description, User creator) {
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.numberOfUsers = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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