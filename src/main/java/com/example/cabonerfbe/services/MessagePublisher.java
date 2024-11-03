package com.example.cabonerfbe.services;

import com.example.cabonerfbe.config.RabbitMQConfig;
import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.request.RabbitMqJsonRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class MessagePublisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value(RabbitMQConfig.EXCHANGE)
    private String exchangeName;

    @Value(RabbitMQConfig.ROUTING_KEY)
    private String routingKey;

    public void sendMessage(RabbitMqJsonRequest request) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, request);
        log.info("Sent message json request: {}", request.toString());
    }

    public void sendMessage(String exchangeName, String routingKey, String request) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, request);
        log.info("Sent message: {}, exchange: {}, key: {}", request, exchangeName, routingKey);
    }

    public void publishCreateProcess(String exchangeName, String routingKey, ProcessDto response) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, response);
        log.info("Publish message: {}, exchange: {}, key: {}", response, exchangeName, routingKey);
    }
}
