package com.srecko.reddit.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;

import javax.persistence.*;

@Entity(name = "Vote")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "votes")
public abstract class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "USER_ID_FK"))
    private User user;

    private VoteType type;

    public Vote(User user, VoteType type) {
        this.user = user;
        this.type = type;
    }

    public Vote() {
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

    public VoteType getType() {
        return type;
    }

    public void setType(VoteType type) {
        this.type = type;
    }
}