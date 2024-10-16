package com.example.caboneftbe.services;

import com.example.caboneftbe.config.RabbitMQConfig;
import com.example.caboneftbe.request.RabbitMqJsonRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageListener {
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handleMessage(RabbitMqJsonRequest request) {
        log.info("Received message: {}", request.toString());
        // Here, you can add logic to process the message.
        // Example: updating a database, calling another service, etc.

        // In the LCADataListener.handleMessage() method,
        // you can implement any logic you want to perform when a message is received. For example:
        // - Save the message data in a database.
        // - Trigger further asynchronous processing.
        // - Communicate with other microservices.
    }
}
