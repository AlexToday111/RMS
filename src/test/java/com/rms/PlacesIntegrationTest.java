package com.rms;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PlacesIntegrationTest extends IntegrationTestBase {
    @Autowired
    private TestRestTemplate rest;

    @Test
    void getPlacesPublic() {
        ResponseEntity<Map> resp = rest.getForEntity("/api/places", Map.class);
        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(resp.getBody()).containsKey("content");
    }
}


