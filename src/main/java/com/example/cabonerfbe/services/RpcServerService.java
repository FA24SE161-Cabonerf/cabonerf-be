package com.example.cabonerfbe.services;

import com.example.cabonerfbe.config.RabbitMQConfig;
import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.request.CreateProcessRequest;
import com.example.cabonerfbe.services.impl.ProcessServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class RpcServerService {

    private final RabbitTemplate rabbitTemplate;
    private final ProcessServiceImpl processService;
    private final ObjectMapper objectMapper;

    @Autowired
    public RpcServerService(RabbitTemplate rabbitTemplate, ProcessServiceImpl processService, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.processService = processService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConfig.RPC_QUEUE)
    public void receiveRpcRequest(Message message) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        String replyTo = message.getMessageProperties().getReplyTo();
        String requestMessage = new String(message.getBody());

        String taskType = extractJsonField(requestMessage, "taskType");

        switch (taskType) {
            case "createProcess" -> handleCreateProcess(requestMessage, correlationId, replyTo);
            case "deleteProcess" -> handleDeleteProcess(requestMessage, correlationId, replyTo);
            default -> sendResponse(replyTo, correlationId, "Unknown task type: " + taskType, false);
        }
    }

    private void handleCreateProcess(String requestMessage, String correlationId, String replyTo) {
        try {
            CreateProcessRequest request = extractCreateProcessRequest(requestMessage);
            ProcessDto processDto = processService.createProcess(request);
            sendResponse(replyTo, correlationId, objectMapper.writeValueAsString(processDto), true);
        } catch (Exception e) {
            logAndSendError(replyTo, correlationId, "Error processing create request", e);
        }
    }

    private void handleDeleteProcess(String requestMessage, String correlationId, String replyTo) {
        try {
            String id = extractJsonField(requestMessage, "data.id");
            sendResponse(replyTo, correlationId, processService.deleteProcess(UUID.fromString(id)).toString(), true);
        } catch (Exception e) {
            logAndSendError(replyTo, correlationId, "Error processing delete request", e);
        }
    }

    private CreateProcessRequest extractCreateProcessRequest(String requestMessage) throws Exception {
        JsonNode dataNode = objectMapper.readTree(requestMessage).path("data");
        return objectMapper.treeToValue(dataNode, CreateProcessRequest.class);
    }

    private String extractJsonField(String jsonMessage, String fieldPath) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonMessage);
            for (String field : fieldPath.split("\\.")) {
                jsonNode = jsonNode.path(field);
            }
            return jsonNode.asText();
        } catch (Exception e) {
            log.error("Failed to extract field {} from message: {}", fieldPath, e.getMessage());
            return null;
        }
    }

    private void sendResponse(String replyTo, String correlationId, String messageContent, boolean isSuccess) {
        try {
            MessageProperties properties = new MessageProperties();
            properties.setCorrelationId(correlationId);
            Message responseMessage = new Message(messageContent.getBytes(), properties);
            rabbitTemplate.convertAndSend(replyTo, responseMessage);
            log.info("{} response sent to queue: {} with correlationId: {}", isSuccess ? "Success" : "Error", replyTo, correlationId);
        } catch (Exception e) {
            log.error("Failed to send {} response: {}", isSuccess ? "success" : "error", e.getMessage());
        }
    }

    private void logAndSendError(String replyTo, String correlationId, String errorMessage, Exception e) {
        log.error("{}: {}", errorMessage, e.getMessage());
        sendResponse(replyTo, correlationId, errorMessage, false);
    }
}
