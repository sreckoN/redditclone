package com.srecko.reddit.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UpdatePostDto {

    @NotNull
    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String text;

    public UpdatePostDto(Long id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    public UpdatePostDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
