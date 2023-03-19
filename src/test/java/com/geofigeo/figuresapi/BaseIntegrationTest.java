package com.geofigeo.figuresapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureMockMvc
public class BaseIntegrationTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper mapper;

    @Container
    private static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:14.1")
                    .withDatabaseName("postgres")
                    .withUsername("postgres")
                    .withPassword("postgress");

    @DynamicPropertySource
    public static void containerConfig(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username",postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password",postgreSQLContainer::getPassword);
    }
}
