package com.istanbul.eminonurehber.DTO;



public class CommentDTO {
    private Long id;
    private String text;
    private int rating;
    private String username;

    public CommentDTO() {
    }

    public CommentDTO(Long id, String text, int rating, String username) {
        this.id = id;
        this.text = text;
        this.rating = rating;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
