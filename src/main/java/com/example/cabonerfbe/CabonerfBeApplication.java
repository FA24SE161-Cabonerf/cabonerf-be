package com.example.cabonerfbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The class Cabonerf be application.
 *
 * @author SonPHH.
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableJpaRepositories
public class CabonerfBeApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(CabonerfBeApplication.class, args);
    }
}
