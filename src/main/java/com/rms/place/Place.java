package com.rms.place;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "places", indexes = {
        @Index(name = "idx_places_location", columnList = "latitude,longitude")
})
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlaceType type;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    // Average crowd-sourced attributes
    @Min(0) @Max(100)
    private Integer noiseLevel;

    @Min(0) @Max(1000)
    private Integer wifiMbps;

    @Min(0) @Max(100)
    private Integer outletAvailability;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlaceType getType() {
        return type;
    }

    public void setType(PlaceType type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Integer getNoiseLevel() {
        return noiseLevel;
    }

    public void setNoiseLevel(Integer noiseLevel) {
        this.noiseLevel = noiseLevel;
    }

    public Integer getWifiMbps() {
        return wifiMbps;
    }

    public void setWifiMbps(Integer wifiMbps) {
        this.wifiMbps = wifiMbps;
    }

    public Integer getOutletAvailability() {
        return outletAvailability;
    }

    public void setOutletAvailability(Integer outletAvailability) {
        this.outletAvailability = outletAvailability;
    }
}


