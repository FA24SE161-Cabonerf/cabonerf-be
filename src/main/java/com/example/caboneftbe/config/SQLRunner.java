package com.example.caboneftbe.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class SQLRunner implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public SQLRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        String sqlScript = loadSQLScript();
        jdbcTemplate.execute(sqlScript);

    }

    private String loadSQLScript() throws IOException {
        ClassPathResource resource = new ClassPathResource("data.sql");
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

}
