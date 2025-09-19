package com.rms.review;

import com.rms.place.Place;
import com.rms.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Place place;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User author;

    @Min(1) @Max(5)
    @Column(nullable = false)
    private int rating;

    @NotBlank
    @Column(nullable = false, length = 1000)
    private String comment;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Long getId() { return id; }
    public Place getPlace() { return place; }
    public void setPlace(Place place) { this.place = place; }
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public Instant getCreatedAt() { return createdAt; }
}


