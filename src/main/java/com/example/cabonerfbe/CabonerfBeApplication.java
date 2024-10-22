package com.example.cabonerfbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CabonerfBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CabonerfBeApplication.class, args);
    }
}
