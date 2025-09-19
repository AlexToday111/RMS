package com.rms;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class IntegrationTestBase {
    @BeforeAll
    static void setup() {
        System.setProperty("spring.jpa.hibernate.ddl-auto", "update");
    }
}


