package com.example.cabonerfbe.services;

import com.example.cabonerfbe.config.RabbitMQConfig;
import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.request.CreateProcessRequest;
import com.example.cabonerfbe.request.RabbitMqJsonRequest;
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

        String taskType = extractTaskType(requestMessage);

        switch (taskType) {
            case "createProcess":
                handleCreateProcess(requestMessage, correlationId, replyTo);
                break;
            case "deleteProcess":
                handleDeleteProcess(requestMessage, correlationId, replyTo);
                break;
            default:
                sendErrorResponse(replyTo, correlationId, "Unknown task type: " + taskType);
        }
    }

    private void handleCreateProcess(String requestMessage, String correlationId, String replyTo) {
        try {
            CreateProcessRequest request = objectMapper.readValue(requestMessage, CreateProcessRequest.class);
            ProcessDto processDto = processService.createProcess(request);
            sendSuccessResponse(replyTo, correlationId, processDto);
        } catch (Exception e) {
            sendErrorResponse(replyTo, correlationId, "Error processing create request: " + e.getMessage());
        }
    }
    
    private void handleDeleteProcess(String requestMessage, String correlationId, String replyTo) {
        try {
            JsonNode jsonNode = objectMapper.readTree(requestMessage);
            String id = jsonNode.path("data").path("id").asText();
            sendSuccessResponse(replyTo, correlationId, processService.deleteProcess(UUID.fromString(id)));
        } catch (Exception e) {
            sendErrorResponse(replyTo, correlationId, "Error processing delete request: " + e.getMessage());
        }
    }

    private void sendSuccessResponse(String replyTo, String correlationId, Object responseObject) {
        try {
            // Convert the response object (e.g., ProcessDto) to JSON
            String responseJson = objectMapper.writeValueAsString(responseObject);

            // Set up the message properties with the correlation ID
            MessageProperties responseProperties = new MessageProperties();
            responseProperties.setCorrelationId(correlationId);

            // Create the response message
            Message responseMessage = new Message(responseJson.getBytes(), responseProperties);

            // Send the response back to the replyTo queue
            rabbitTemplate.convertAndSend(replyTo, responseMessage);

            log.info("Success response sent to queue: {} with correlationId: {}", replyTo, correlationId);
        } catch (Exception e) {
            log.error("Failed to send success response: {}", e.getMessage());
            // Optionally, send an error response here or handle the exception
        }
    }

    private void sendErrorResponse(String replyTo, String correlationId, String errorMessage) {
        MessageProperties responseProperties = new MessageProperties();
        responseProperties.setCorrelationId(correlationId);
        Message errorResponse = new Message(errorMessage.getBytes(), responseProperties);
        rabbitTemplate.convertAndSend(replyTo, errorResponse);
    }

    private String extractTaskType(String requestMessage) {
        try {
            // Parse the request message as JSON
            JsonNode jsonNode = objectMapper.readTree(requestMessage);

            // Extract the taskType field
            return jsonNode.get("taskType").asText();
        } catch (Exception e) {
            // Handle exceptions (e.g., log error and return a default task type or throw an exception)
            log.error("Failed to extract task type from message: {}", e.getMessage());
            return "unknown"; // or throw an exception if necessary
        }
    }

}
