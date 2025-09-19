package com.rms;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthIntegrationTest extends IntegrationTestBase {
    @Autowired
    private TestRestTemplate rest;

    @Test
    void registerAndLogin() {
        var registerBody = Map.of("email", "test@example.com", "password", "secret123");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Map> regResp = rest.exchange("/api/auth/register", HttpMethod.POST, new HttpEntity<>(registerBody, headers), Map.class);
        assertThat(regResp.getStatusCode().is2xxSuccessful()).isTrue();

        var loginBody = Map.of("email", "test@example.com", "password", "secret123");
        ResponseEntity<Map> loginResp = rest.exchange("/api/auth/login", HttpMethod.POST, new HttpEntity<>(loginBody, headers), Map.class);
        assertThat(loginResp.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(loginResp.getBody()).containsKey("token");
    }
}


