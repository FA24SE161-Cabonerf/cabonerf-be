package com.example.cabonerfbe.services;

import com.example.cabonerfbe.config.RabbitMQConfig;
import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.request.AddObjectLibraryRequest;
import com.example.cabonerfbe.request.CreateConnectorRequest;
import com.example.cabonerfbe.request.CreateProcessRequest;
import com.example.cabonerfbe.response.CreateConnectorResponse;
import com.example.cabonerfbe.response.DeleteConnectorResponse;
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

/**
 * The class Rpc server service.
 *
 * @author SonPHH.
 */
@Service
@Slf4j
public class RpcServerService {

    /**
     * The constant PROCESS_QUEUE_TYPE.
     */
    public static final String PROCESS_QUEUE_TYPE = "process";
    /**
     * The constant CONNECTOR_QUEUE_TYPE.
     */
    public static final String CONNECTOR_QUEUE_TYPE = "connector";
    private final RabbitTemplate rabbitTemplate;
    private final ProcessServiceImpl processService;
    private final ConnectorServiceImpl connectorService;
    private final ObjectMapper objectMapper;
    private final ObjectLibraryService objectLibraryService;

    /**
     * Instantiates a new Rpc server service.
     *
     * @param rabbitTemplate       the rabbit template
     * @param processService       the process service
     * @param connectorService     the connector service
     * @param objectMapper         the object mapper
     * @param objectLibraryService the object library service
     */
    @Autowired
    public RpcServerService(RabbitTemplate rabbitTemplate, ProcessServiceImpl processService, ConnectorServiceImpl connectorService, ObjectMapper objectMapper, ObjectLibraryService objectLibraryService) {
        this.rabbitTemplate = rabbitTemplate;
        this.processService = processService;
        this.connectorService = connectorService;
        this.objectMapper = objectMapper;
        this.objectLibraryService = objectLibraryService;
    }

    /**
     * Receive process tasks method.
     *
     * @param message the message
     */
    @RabbitListener(queues = RabbitMQConfig.RPC_QUEUE)
    public void receiveProcessTasks(Message message) {
        processTask(message, PROCESS_QUEUE_TYPE);
    }

    /**
     * Receive connector tasks method.
     *
     * @param message the message
     */
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
            case "addObjectLibrary" -> {
                if (PROCESS_QUEUE_TYPE.equals(queueType)) {
                    handleAddObjectLibrary(requestMessage, correlationId, replyTo);
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

    private void handleAddObjectLibrary(String requestMessage, String correlationId, String replyTo) {
        try {
            AddObjectLibraryRequest request = extractRequest(requestMessage, AddObjectLibraryRequest.class);
            ProcessDto processDto = objectLibraryService.addFromObjectLibraryToProject(request);
            sendResponse(replyTo, correlationId, objectMapper.writeValueAsString(processDto), true);
        } catch (CustomExceptions e) { // Catch the custom exception
            logAndSendCustomError(replyTo, correlationId, "Business error: " + e.getError().getMessage(), e);
        } catch (Exception e) {
            logAndSendError(replyTo, correlationId, "Error processing create process request", e);
        }
    }

    private void sendUnknownTaskResponse(String replyTo, String correlationId, String taskType, String queueType) {
        sendResponse(replyTo, correlationId, "Unknown task type for " + queueType + " queue: " + taskType, false);
    }

    private void handleCreateConnector(String requestMessage, String correlationId, String replyTo) {
        try {
            CreateConnectorRequest request = extractRequest(requestMessage, CreateConnectorRequest.class);
            log.error("request after extract: {}, raw: {}", request.toString(), request);
            CreateConnectorResponse response = connectorService.createConnector(request);
            log.error("response: {}, raw: {}", response.toString(), response);
            sendResponse(replyTo, correlationId, objectMapper.writeValueAsString(response), true);
        } catch (CustomExceptions e) { // Catch the custom exception
            logAndSendCustomError(replyTo, correlationId, "Business error: " + e.getError().getMessage(), e);
        } catch (Exception e) {
            logAndSendError(replyTo, correlationId, "Error processing create connector request", e);
        }
    }

    private void handleDeleteConnector(String requestMessage, String correlationId, String replyTo) {
        try {
            String id = extractJsonField(requestMessage, "data.id");
            DeleteConnectorResponse response = connectorService.deleteConnector(UUID.fromString(id));
            sendResponse(replyTo, correlationId, objectMapper.writeValueAsString(response), true);
        } catch (CustomExceptions e) { // Catch the custom exception
            logAndSendCustomError(replyTo, correlationId, "Business error: " + e.getError().getMessage(), e);
        } catch (Exception e) {
            logAndSendError(replyTo, correlationId, "Error processing delete connector request", e);
        }
    }

    private void handleCreateProcess(String requestMessage, String correlationId, String replyTo) {
        try {
            CreateProcessRequest request = extractRequest(requestMessage, CreateProcessRequest.class);
            ProcessDto processDto = processService.createProcess(request);
            sendResponse(replyTo, correlationId, objectMapper.writeValueAsString(processDto), true);
        } catch (CustomExceptions e) { // Catch the custom exception
            logAndSendCustomError(replyTo, correlationId, "Business error: " + e.getError().getMessage(), e);
        } catch (Exception e) {
            logAndSendError(replyTo, correlationId, "Error processing create process request", e);
        }
    }

    private void handleDeleteProcess(String requestMessage, String correlationId, String replyTo) {
        try {
            String id = extractJsonField(requestMessage, "data.id");
            DeleteProcessResponse deleteProcessResponse = processService.deleteProcess(UUID.fromString(id));
            sendResponse(replyTo, correlationId, objectMapper.writeValueAsString(deleteProcessResponse), true);
        } catch (CustomExceptions e) { // Catch the custom exception
            logAndSendCustomError(replyTo, correlationId, "Business error: " + e.getError().getMessage(), e);
        } catch (Exception e) {
            logAndSendError(replyTo, correlationId, "Error processing delete process request", e);
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
        System.out.println("data node:" + dataNode);
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
        log.error("{}: {}", errorMessage, e.getCause());
        sendResponse(replyTo, correlationId, errorMessage, false);
    }

    private void logAndSendCustomError(String replyTo, String correlationId, String errorMessage, CustomExceptions e) {
        log.error("{}: {}", errorMessage, e.getError().getMessage());
        sendResponse(replyTo, correlationId, errorMessage, false);
    }
}
