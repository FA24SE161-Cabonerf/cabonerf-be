package com.example.cabonerfbe.services;

import com.example.cabonerfbe.config.RabbitMQConfig;
import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.request.CreateProcessImpactValueRequest;
import com.example.cabonerfbe.request.RabbitMqJsonRequest;
import com.example.cabonerfbe.response.SendMailCreateAccountOrganizationResponse;
import com.example.cabonerfbe.response.SendMailRegisterResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class MessagePublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value(RabbitMQConfig.EXCHANGE)
    private String exchangeName;

    @Value(RabbitMQConfig.ROUTING_KEY)
    private String routingKey;

    public MessagePublisher(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

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

    public void publishCreateProcessImpactValue(String exchangeName, String routingKey, UUID processId, UUID methodId) {
        CreateProcessImpactValueRequest createProcessImpactValueRequest = new CreateProcessImpactValueRequest(processId, methodId);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, createProcessImpactValueRequest);
        log.info("Publish create impact value message: {}, exchange: {}, key: {}", createProcessImpactValueRequest, exchangeName, routingKey);
    }

    public void publishConnectorMessage(String exchangeName, String routingKey, List<UUID> idList) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, idList);
        log.info("Publish connector message: {}, exchange: {}, key: {}", idList, exchangeName, routingKey);
    }

    public void publishSendMailCreateOrganization(SendMailCreateAccountOrganizationResponse response) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_EXCHANGE, RabbitMQConfig.EMAIL_CREATE_ORGANIZATION_ROUTING_KEY, response);
        log.info("Publish SendMailCreateOrganization: {}", response.getEmail());
    }

//    public void publishSendMailCreateAccountByOrganizationManager(SendMailCreateAccountResponse response) {
//        rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_EXCHANGE, RabbitMQConfig.EMAIL_CREATE_ACCOUNT_MANAGER_ROUTING_KEY, response);
//        log.info("Publish SendMailCreateAccountByOrganizationManager: {}", response.getEmail());
//    }

    public void publishSendMailRegister(SendMailRegisterResponse response) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_EXCHANGE, RabbitMQConfig.EMAIL_REGISTER_ROUTING_KEY, response);
        log.info("Publish SendMailRegister: {}", response.getEmail());
    }
}
