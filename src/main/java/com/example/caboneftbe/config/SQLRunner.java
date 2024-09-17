package com.example.caboneftbe.config;

import com.example.caboneftbe.models.Users;
import com.example.caboneftbe.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class SQLRunner implements CommandLineRunner {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    private final JdbcTemplate jdbcTemplate;

    public SQLRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        String sqlScript = loadSQLScript();
        jdbcTemplate.execute(sqlScript);
//        String pass = passwordEncoder.encode("12345");
//        Users users = new Users();
//        users.setPassword(pass);
//        users.setEmail("cabonerf@gmail.com");
//        userRepository.save(users);

    }

    private String loadSQLScript() throws IOException {
        ClassPathResource resource = new ClassPathResource("data.sql");
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

}
