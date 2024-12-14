package com.example.cabonerfbe.services;

import com.example.cabonerfbe.config.RabbitMQConfig;
import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.request.CreateProcessImpactValueRequest;
import com.example.cabonerfbe.request.RabbitMqJsonRequest;
import com.example.cabonerfbe.request.SendMailCreateAccountOrganizationRequest;
import com.example.cabonerfbe.request.SendMailInviteRequest;
import com.example.cabonerfbe.response.SendMailRegisterResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * The class Message publisher.
 *
 * @author SonPHH.
 */
@Service
@Slf4j
public class MessagePublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value(RabbitMQConfig.EXCHANGE)
    private String exchangeName;

    @Value(RabbitMQConfig.ROUTING_KEY)
    private String routingKey;

    /**
     * Instantiates a new Message publisher.
     *
     * @param rabbitTemplate the rabbit template
     */
    public MessagePublisher(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Send message method.
     *
     * @param request the request
     */
    public void sendMessage(RabbitMqJsonRequest request) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, request);
        log.info("Sent message json request: {}", request);
    }

    /**
     * Send message method.
     *
     * @param exchangeName the exchange name
     * @param routingKey   the routing key
     * @param request      the request
     */
    public void sendMessage(String exchangeName, String routingKey, String request) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, request);
        log.info("Sent message: {}, exchange: {}, key: {}", request, exchangeName, routingKey);
    }

    /**
     * Publish create process method.
     *
     * @param exchangeName the exchange name
     * @param routingKey   the routing key
     * @param response     the response
     */
    public void publishCreateProcess(String exchangeName, String routingKey, ProcessDto response) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, response);
        log.info("Publish message: {}, exchange: {}, key: {}", response, exchangeName, routingKey);
    }

    /**
     * Publish create process impact value method.
     *
     * @param exchangeName the exchange name
     * @param routingKey   the routing key
     * @param processId    the process id
     * @param methodId     the method id
     */
    public void publishCreateProcessImpactValue(String exchangeName, String routingKey, UUID processId, UUID methodId) {
        CreateProcessImpactValueRequest createProcessImpactValueRequest = new CreateProcessImpactValueRequest(processId, methodId);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, createProcessImpactValueRequest);
        log.info("Publish create impact value message: {}, exchange: {}, key: {}", createProcessImpactValueRequest, exchangeName, routingKey);
    }

    /**
     * Publish connector message method.
     *
     * @param exchangeName the exchange name
     * @param routingKey   the routing key
     * @param idList       the id list
     */
    public void publishConnectorMessage(String exchangeName, String routingKey, List<UUID> idList) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, idList);
        log.info("Publish connector message: {}, exchange: {}, key: {}", idList, exchangeName, routingKey);
    }

    /**
     * Publish send mail create account organization method.
     *
     * @param request the response
     */
    public void publishSendMailCreateAccountOrganization(SendMailCreateAccountOrganizationRequest request) {
        log.info("Publish SendMailCreateAccountOrganization: {}", request.getEmail());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_EXCHANGE, RabbitMQConfig.EMAIL_CREATE_ORGANIZATION_ROUTING_KEY, request);
    }

    /**
     * Publish send mail invite to organization method.
     *
     * @param request the request
     */
    public void publishSendMailInviteToOrganization(SendMailInviteRequest request) {
        log.info("Publish publishSendMailInviteToOrganization: {}", request);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_EXCHANGE, RabbitMQConfig.EMAIL_INVITE_ROUTING_KEY, request);
    }

    /**
     * Publish send mail register method.
     *
     * @param response the response
     */
    public void publishSendMailRegister(SendMailRegisterResponse response) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_EXCHANGE, RabbitMQConfig.EMAIL_REGISTER_ROUTING_KEY, response);
        log.info("Publish SendMailRegister: {}", response.getEmail());
    }
}
