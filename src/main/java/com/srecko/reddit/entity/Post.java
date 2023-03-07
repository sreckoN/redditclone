package com.srecko.reddit.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    private Date dateOfCreation;

    @NotEmpty
    @Size(min = 3, max = 35)
    private String title;

    @NotEmpty
    @Size(min = 2, max = 1000)
    private String text;

    private int votes;

    private int commentsCounter;

    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    private User user;

    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    private Subreddit subreddit;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "post", fetch = FetchType.LAZY)
    private List<Comment> comments;

    public Post() {
    }

    public Post(User createdBy, String title, String text, Subreddit subreddit) {
        this.user = createdBy;
        this.title = title;
        this.text = text;
        this.subreddit = subreddit;
        this.dateOfCreation = new Date();
        this.votes = 0;
        this.commentsCounter = 0;
        this.comments = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
