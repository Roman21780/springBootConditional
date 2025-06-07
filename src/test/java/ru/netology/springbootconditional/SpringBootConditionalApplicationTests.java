package ru.netology.springbootconditional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus; // Добавлен этот импорт
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class SpringBootConditionalApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    private static final GenericContainer<?> devApp = new GenericContainer<>("devapp:latest")
            .withExposedPorts(8080)
            .withEnv("netology.profile.dev", "true")
            .waitingFor(Wait.forHttp("/profile")
                    .forStatusCode(200)
                    .withStartupTimeout(Duration.ofSeconds(60)));

    @Container
    private static final GenericContainer<?> prodApp = new GenericContainer<>("prodapp:latest")
            .withExposedPorts(8081)
            .withEnv("netology.profile.dev", "false")
            .waitingFor(Wait.forHttp("/profile")
                    .forStatusCode(200)
                    .withStartupTimeout(Duration.ofSeconds(60)));

    @Test
    void devProfileTest() {
        String url = "http://localhost:" + devApp.getMappedPort(8080) + "/profile";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode()); // Теперь должно работать
        assertEquals("Current profile is dev", response.getBody());
    }

    @Test
    void prodProfileTest() {
        String url = "http://localhost:" + prodApp.getMappedPort(8081) + "/profile";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode()); // Теперь должно работать
        assertEquals("Current profile is production", response.getBody());
    }
}