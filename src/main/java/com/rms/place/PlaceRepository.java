package com.rms.place;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    @Query("select p from Place p where (:type is null or p.type = :type) " +
            "and (:minNoise is null or p.noiseLevel >= :minNoise) " +
            "and (:maxNoise is null or p.noiseLevel <= :maxNoise) " +
            "and (:minWifi is null or p.wifiMbps >= :minWifi) " +
            "and (:minOutlets is null or p.outletAvailability >= :minOutlets)")
    Page<Place> search(
            @Param("type") PlaceType type,
            @Param("minNoise") Integer minNoise,
            @Param("maxNoise") Integer maxNoise,
            @Param("minWifi") Integer minWifi,
            @Param("minOutlets") Integer minOutlets,
            Pageable pageable);

    // Simple Haversine distance sort/filter using lat/lon (not PostGIS). Radius in meters.
    @Query(value = "SELECT p.* " +
            "FROM places p " +
            "WHERE (6371000 * 2 * ASIN(SQRT(POWER(SIN(RADIANS((:lat - p.latitude) / 2)), 2) + COS(RADIANS(:lat)) * COS(RADIANS(p.latitude)) * POWER(SIN(RADIANS((:lon - p.longitude) / 2)), 2)))) <= :radius ",
            countQuery = "SELECT count(*) FROM places p WHERE (6371000 * 2 * ASIN(SQRT(POWER(SIN(RADIANS((:lat - p.latitude) / 2)), 2) + COS(RADIANS(:lat)) * COS(RADIANS(p.latitude)) * POWER(SIN(RADIANS((:lon - p.longitude) / 2)), 2)))) <= :radius ",
            nativeQuery = true)
    Page<Place> searchByRadius(@Param("lat") double lat,
                               @Param("lon") double lon,
                               @Param("radius") double radius,
                               Pageable pageable);
}


