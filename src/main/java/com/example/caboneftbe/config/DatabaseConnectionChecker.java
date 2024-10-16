package com.example.caboneftbe.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@Slf4j
public class DatabaseConnectionChecker implements ApplicationListener<ApplicationReadyEvent> {
    @PersistenceContext
    private EntityManager entityManager;

    private final DataSource dataSource;

    public DatabaseConnectionChecker(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                log.info("Connect database successfully");
            }
        } catch (SQLException e) {
            log.error("Connect database failed: {}", e.getMessage());
        }
    }
}
