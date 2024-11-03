package com.example.cabonerfbe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync(proxyTargetClass = true)
@Configuration
public class AsyncConfig {
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(999);
        executor.setMaxPoolSize(9999);
        executor.setQueueCapacity(9999);
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}
