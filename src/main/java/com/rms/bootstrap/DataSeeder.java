package com.rms.bootstrap;

import com.rms.place.Place;
import com.rms.place.PlaceRepository;
import com.rms.place.PlaceType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"default"})
public class DataSeeder implements CommandLineRunner {
    private final PlaceRepository placeRepository;

    public DataSeeder(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Override
    public void run(String... args) {
        if (placeRepository.count() > 0) return;
        Place p1 = new Place();
        p1.setName("Local Cafe");
        p1.setType(PlaceType.CAFE);
        p1.setLatitude(55.751244);
        p1.setLongitude(37.618423);
        p1.setNoiseLevel(40);
        p1.setWifiMbps(100);
        p1.setOutletAvailability(60);

        Place p2 = new Place();
        p2.setName("City Library");
        p2.setType(PlaceType.LIBRARY);
        p2.setLatitude(55.760245);
        p2.setLongitude(37.620423);
        p2.setNoiseLevel(20);
        p2.setWifiMbps(50);
        p2.setOutletAvailability(80);

        Place p3 = new Place();
        p3.setName("Coworking Hub");
        p3.setType(PlaceType.COWORKING);
        p3.setLatitude(55.745244);
        p3.setLongitude(37.600423);
        p3.setNoiseLevel(35);
        p3.setWifiMbps(200);
        p3.setOutletAvailability(90);

        placeRepository.save(p1);
        placeRepository.save(p2);
        placeRepository.save(p3);
    }
}


