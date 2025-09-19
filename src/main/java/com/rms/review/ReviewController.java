package com.rms.review;

import com.rms.place.Place;
import com.rms.place.PlaceRepository;
import com.rms.user.User;
import com.rms.user.UserRepository;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
public class ReviewController {
    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    public ReviewController(ReviewRepository reviewRepository, PlaceRepository placeRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
    }

    public static class CreateReviewRequest {
        public Long placeId;
        @Min(1) @Max(5)
        public int rating;
        @NotBlank
        public String comment;
    }

    @GetMapping("/spots/{placeId}/reviews")
    public ResponseEntity<List<Review>> listByPlace(@PathVariable Long placeId) {
        Place place = placeRepository.findById(placeId).orElseThrow();
        return ResponseEntity.ok(reviewRepository.findByPlace(place));
    }

    @PostMapping("/spots/{placeId}/reviews")
    public ResponseEntity<Review> create(@PathVariable Long placeId, @RequestBody CreateReviewRequest req, Authentication auth) {
        Place place = placeRepository.findById(placeId).orElseThrow();
        Review r = new Review();
        r.setPlace(place);
        String email = auth.getName();
        User author = userRepository.findByEmail(email).orElseThrow();
        r.setAuthor(author);
        r.setRating(req.rating);
        r.setComment(req.comment);
        return ResponseEntity.ok(reviewRepository.save(r));
    }

    @PutMapping("/reviews/{id}")
    public ResponseEntity<Review> update(@PathVariable Long id, @RequestBody CreateReviewRequest req, Authentication auth) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review == null) return ResponseEntity.notFound().build();
        boolean isOwner = Objects.equals(auth.getName(), review.getAuthor().getEmail());
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isOwner && !isAdmin) return ResponseEntity.status(403).build();
        if (req.comment != null && !req.comment.isBlank()) review.setComment(req.comment);
        if (req.rating >= 1 && req.rating <= 5) review.setRating(req.rating);
        return ResponseEntity.ok(reviewRepository.save(review));
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication auth) {
        Review review = reviewRepository.findById(id).orElse(null);
        if (review == null) return ResponseEntity.notFound().build();
        boolean isOwner = Objects.equals(auth.getName(), review.getAuthor().getEmail());
        boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isOwner && !isAdmin) return ResponseEntity.status(403).build();
        reviewRepository.delete(review);
        return ResponseEntity.noContent().build();
    }
}


