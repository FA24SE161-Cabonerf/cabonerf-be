package com.example.cabonerfbe.services;

import com.example.cabonerfbe.config.RabbitMQConfig;
import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.request.CreateConnectorRequest;
import com.example.cabonerfbe.request.CreateProcessRequest;
import com.example.cabonerfbe.response.CreateConnectorResponse;
import com.example.cabonerfbe.response.DeleteProcessResponse;
import com.example.cabonerfbe.services.impl.ConnectorServiceImpl;
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
    private final ConnectorServiceImpl connectorService;
    private final ObjectMapper objectMapper;
    public static final String PROCESS_QUEUE_TYPE = "process";
    public static final String CONNECTOR_QUEUE_TYPE = "connector";

    @Autowired
    public RpcServerService(RabbitTemplate rabbitTemplate, ProcessServiceImpl processService, ConnectorServiceImpl connectorService, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.processService = processService;
        this.connectorService = connectorService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConfig.RPC_QUEUE)
    public void receiveProcessTasks(Message message) {
        processTask(message, PROCESS_QUEUE_TYPE);
    }

    @RabbitListener(queues = RabbitMQConfig.RPC_QUEUE_CONNECTOR)
    public void receiveConnectorTasks(Message message) {
        processTask(message, CONNECTOR_QUEUE_TYPE);
    }

    private void processTask(Message message, String queueType) {
        String correlationId = message.getMessageProperties().getCorrelationId();
        String replyTo = message.getMessageProperties().getReplyTo();
        String requestMessage = new String(message.getBody());

        String taskType = extractJsonField(requestMessage, "taskType");

        switch (taskType) {
            case "createProcess" -> {
                if (PROCESS_QUEUE_TYPE.equals(queueType)) {
                    handleCreateProcess(requestMessage, correlationId, replyTo);
                } else {
                    sendUnknownTaskResponse(replyTo, correlationId, taskType, queueType);
                }
            }
            case "deleteProcess" -> {
                if (PROCESS_QUEUE_TYPE.equals(queueType)) {
                    handleDeleteProcess(requestMessage, correlationId, replyTo);
                } else {
                    sendUnknownTaskResponse(replyTo, correlationId, taskType, queueType);
                }
            }
            case "createConnector" -> {
                if (CONNECTOR_QUEUE_TYPE.equals(queueType)) {
                    handleCreateConnector(requestMessage, correlationId, replyTo);
                } else {
                    sendUnknownTaskResponse(replyTo, correlationId, taskType, queueType);
                }
            }
            case "deleteConnector" -> {
                if (CONNECTOR_QUEUE_TYPE.equals(queueType)) {
                    handleDeleteConnector(requestMessage, correlationId, replyTo);
                } else {
                    sendUnknownTaskResponse(replyTo, correlationId, taskType, queueType);
                }
            }
            default -> sendUnknownTaskResponse(replyTo, correlationId, taskType, queueType);
        }
    }

    private void sendUnknownTaskResponse(String replyTo, String correlationId, String taskType, String queueType) {
        sendResponse(replyTo, correlationId, "Unknown task type for " + queueType + " queue: " + taskType, false);
    }

    private void handleCreateConnector(String requestMessage, String correlationId, String replyTo) {
        try {
            CreateConnectorRequest request = extractRequest(requestMessage, CreateConnectorRequest.class);
            CreateConnectorResponse response = connectorService.createConnector(request);
            sendResponse(replyTo, correlationId, objectMapper.writeValueAsString(response), true);
        } catch (Exception e) {
            logAndSendError(replyTo, correlationId, "Error processing create request", e);
        }
    }


    private void handleDeleteConnector(String requestMessage, String correlationId, String replyTo) {

    }

    private void handleCreateProcess(String requestMessage, String correlationId, String replyTo) {
        try {
            CreateProcessRequest request = extractRequest(requestMessage, CreateProcessRequest.class);
            ProcessDto processDto = processService.createProcess(request);
            sendResponse(replyTo, correlationId, objectMapper.writeValueAsString(processDto), true);
        } catch (Exception e) {
            logAndSendError(replyTo, correlationId, "Error processing create request", e);
        }
    }

    private void handleDeleteProcess(String requestMessage, String correlationId, String replyTo) {
        try {
            String id = extractJsonField(requestMessage, "data.id");
            DeleteProcessResponse deleteProcessResponse = processService.deleteProcess(UUID.fromString(id));
            sendResponse(replyTo, correlationId, objectMapper.writeValueAsString(deleteProcessResponse), true);
        } catch (Exception e) {
            logAndSendError(replyTo, correlationId, "Error processing delete request", e);
        }
    }

//    private CreateProcessRequest extractCreateProcessRequest(String requestMessage) throws Exception {
//        JsonNode dataNode = objectMapper.readTree(requestMessage).path("data");
//        return objectMapper.treeToValue(dataNode, CreateProcessRequest.class);
//    }
//
//    private CreateConnectorRequest extractCreateConnectorRequest(String requestMessage) throws Exception {
//        JsonNode dataNode = objectMapper.readTree(requestMessage).path("data");
//        return objectMapper.treeToValue(dataNode, CreateConnectorRequest.class);
//    }

    private <T> T extractRequest(String requestMessage, Class<T> requestType) throws Exception {
        JsonNode dataNode = objectMapper.readTree(requestMessage).path("data");
        return objectMapper.treeToValue(dataNode, requestType);
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
