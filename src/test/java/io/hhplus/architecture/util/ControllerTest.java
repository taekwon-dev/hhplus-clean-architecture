package io.hhplus.architecture.util;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private DatabaseCleaner databaseCleaner;


    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleaner.execute();
    }
}
