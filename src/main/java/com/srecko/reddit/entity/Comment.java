package com.srecko.reddit.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue
    private Long id;
    @NotEmpty
    @Size(min = 2, max = 500)
    private String text;
    private int votes;
    private Date created;
    @JsonIdentityReference(alwaysAsId = true)
    @NotNull
    @ManyToOne
    private User user;
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    private Post post;

    public Comment() {
    }

    public Comment(User user, String text, Post post) {
        this.user = user;
        this.text = text;
        this.post = post;
        this.votes = 0;
        this.created = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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