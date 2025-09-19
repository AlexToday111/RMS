package com.rms.place;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/spots")
public class PlaceController {
    private final PlaceRepository placeRepository;

    public PlaceController(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @GetMapping
    public ResponseEntity<Page<Place>> search(
            @RequestParam(required = false) PlaceType type,
            @RequestParam(required = false) Integer minNoise,
            @RequestParam(required = false) Integer maxNoise,
            @RequestParam(required = false) Integer minWifi,
            @RequestParam(required = false) Integer minOutlets,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(placeRepository.search(type, minNoise, maxNoise, minWifi, minOutlets, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Place> getById(@PathVariable Long id) {
        return placeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Place>> searchByRadius(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam double radius,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(placeRepository.searchByRadius(lat, lon, radius, pageable));
    }
}


