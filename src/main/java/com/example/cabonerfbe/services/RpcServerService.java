package com.example.cabonerfbe.services;

import com.example.cabonerfbe.config.RabbitMQConfig;
import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.request.CreateProcessRequest;
import com.example.cabonerfbe.request.RabbitMqJsonRequest;
import com.example.cabonerfbe.services.impl.ProcessServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RpcServerService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ProcessServiceImpl processService;
    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.RPC_QUEUE)
    public void receiveRpcRequest(Message message) {
        // Extract correlationId and replyTo from the received message
        String correlationId = message.getMessageProperties().getCorrelationId();
        String replyTo = message.getMessageProperties().getReplyTo();
        String requestMessage = new String(message.getBody());

        log.info("request msg: {}", requestMessage);

        CreateProcessRequest request;
        try {
            // Convert the message body to CreateProcessRequest
            request = objectMapper.readValue(message.getBody(), CreateProcessRequest.class);
            log.info("request sau khi convert: {}", request);
        } catch (Exception e) {
            // Handle the exception, e.g., log an error and return an error response
            String errorMessage = "Invalid request format: " + e.getMessage();
            sendErrorResponse(replyTo, correlationId, errorMessage);
            return;
        }

        log.info("start create process through rpc queue");
        ProcessDto processDto = processService.createProcess(request);
        log.info("created process: {}", processDto);
        try {
            // Convert ProcessDto to JSON for response
            String responseJson = objectMapper.writeValueAsString(processDto);

            // Send the response back to the replyTo queue with the same correlationId
            MessageProperties responseProperties = new MessageProperties();
            responseProperties.setCorrelationId(correlationId);
            Message response = new Message(responseJson.getBytes(), responseProperties);
            log.info("response json: {}", responseJson);
            log.info("response amqp: {}", response);
            rabbitTemplate.convertAndSend(replyTo, response);
        } catch (Exception e) {
            // Handle serialization error
            String errorMessage = "Error serializing response: " + e.getMessage();
            sendErrorResponse(replyTo, correlationId, errorMessage);
        }
    }

    private void sendErrorResponse(String replyTo, String correlationId, String errorMessage) {
        log.info("loi vcc");
        MessageProperties responseProperties = new MessageProperties();
        responseProperties.setCorrelationId(correlationId);
        Message errorResponse = new Message(errorMessage.getBytes(), responseProperties);
        rabbitTemplate.convertAndSend(replyTo, errorResponse);
    }
}
