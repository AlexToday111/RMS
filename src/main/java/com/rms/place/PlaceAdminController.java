package com.rms.place;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/spots")
public class PlaceAdminController {
    private final PlaceRepository placeRepository;

    public PlaceAdminController(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Place> create(@RequestBody Place place) {
        return ResponseEntity.ok(placeRepository.save(place));
    }
}


