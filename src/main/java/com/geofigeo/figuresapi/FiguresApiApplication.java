package com.geofigeo.figuresapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class FiguresApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FiguresApiApplication.class, args);
    }

}
