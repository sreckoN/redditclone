package com.srecko.reddit.dto;

public class PostDto {

    private Long id;
    private String subredditName;
    private String title;
    private String text;

    public PostDto(Long id, String subredditName, String title, String text) {
        this.id = id;
        this.subredditName = subredditName;
        this.title = title;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubredditName() {
        return subredditName;
    }

    public void setSubredditName(String subredditName) {
        this.subredditName = subredditName;
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
}
