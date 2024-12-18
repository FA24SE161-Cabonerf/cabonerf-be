package com.example.cabonerfbe.services;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * The class Rabbit mq test.
 *
 * @author SonPHH.
 */
@Component
public class RabbitMqTest implements CommandLineRunner {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Override
    public void run(String... args) {
        try {
            String message = "Hello RabbitMQ!";
            System.out.println("Message sent: " + message);
        } catch (Exception e) {
            System.err.println("Failed to send message: " + e.getMessage());
        }
    }
}