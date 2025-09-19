package com.rms.review;

import com.rms.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByPlace(Place place);
}


