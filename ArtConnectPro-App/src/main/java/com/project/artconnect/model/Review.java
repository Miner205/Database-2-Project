package com.project.artconnect.model;

import java.time.LocalDate;

public class Review {
    private int reviewerId;
    private int artworkId;
    private int rating; // 1-5
    private String comment;
    private LocalDate reviewDate;

    public Review() {
    }

    public Review(int reviewerId, int artworkId, int rating, String comment) {
        this.reviewerId = reviewerId;
        this.artworkId = artworkId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = LocalDate.now();
    }

    public int getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(int reviewerId) {
        this.reviewerId = reviewerId;
    }

    public int getArtworkId() {
        return artworkId;
    }

    public void setArtworkId(int artworkId) {
        this.artworkId = artworkId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }
}
