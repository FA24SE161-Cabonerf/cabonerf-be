package com.example.cabonerfbe.services;

import com.example.cabonerfbe.config.RabbitMQConfig;
import com.example.cabonerfbe.request.RabbitMqJsonRequest;
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

    @RabbitListener(queues = RabbitMQConfig.RPC_QUEUE)
    public void receiveRpcRequest(Message message) {
        // Extract correlationId and replyTo from the received message
        String correlationId = message.getMessageProperties().getCorrelationId();
        String replyTo = message.getMessageProperties().getReplyTo();

        String requestMessage = new String(message.getBody());

        // Process the request (you can implement your own processing logic here)
        String responseMessage = "Processed: " + requestMessage;

        // Send the response back to the replyTo queue with the same correlationId
        MessageProperties responseProperties = new MessageProperties();
        responseProperties.setCorrelationId(correlationId);

        Message response = new Message(responseMessage.getBytes(), responseProperties);
        rabbitTemplate.send(replyTo, response);
    }

}
