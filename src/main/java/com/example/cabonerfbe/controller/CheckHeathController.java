package com.example.cabonerfbe.controller;

import com.example.cabonerfbe.enums.API_PARAMS;
import com.example.cabonerfbe.request.RabbitMqJsonRequest;
import com.example.cabonerfbe.services.MessagePublisher;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The class Check heath controller.
 *
 * @author SonPHH.
 */
@RequestMapping(API_PARAMS.API_VERSION)
@NoArgsConstructor(force = true)
@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
@Slf4j
public class CheckHeathController {
    @Autowired
    private MessagePublisher messagePublisher;

    /**
     * Health method.
     *
     * @return the response entity
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        log.info("Start health.");
        return ResponseEntity.ok().body("OK");
    }

    /**
     * Send message method.
     *
     * @param request the request
     * @return the response entity
     */
    @GetMapping("/send-message")
    public ResponseEntity<String> sendMessage(@RequestBody RabbitMqJsonRequest request) {
        log.info("Start send message to rabbitmq. request: {}", request);
        messagePublisher.sendMessage(request);
        // mỗi khi call thì sẽ send message to rabbitMQ
        // send message xong thì message listener sẽ bắt (message) và xử lí
        return ResponseEntity.ok().body("Test send message - queued by rabbitMQ");
    }

    /**
     * Test create process queue method.
     *
     * @param string the string
     * @return the response entity
     */
    @PostMapping("/test-create-process-queue")
    public ResponseEntity<String> testCreateProcessQueue(@RequestBody String string) {
        log.info("Start send message to rabbitmq with create process queue. request: {}", string);
        messagePublisher.sendMessage("create.process.exchange", "create.process.key", string);
        return ResponseEntity.ok().body("OK");
    }
}
