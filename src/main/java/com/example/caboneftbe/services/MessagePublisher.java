package com.example.caboneftbe.services;

import com.example.caboneftbe.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessagePublisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value(RabbitMQConfig.EXCHANGE)
    private String exchangeName;

    @Value(RabbitMQConfig.ROUTING_KEY)
    private String routingKey;

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
        log.info("Sent message: {}", message);
    }
}
