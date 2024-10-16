package com.example.caboneftbe.controller;

import com.example.caboneftbe.enums.API_PARAMS;
import com.example.caboneftbe.request.RabbitMqJsonRequest;
import com.example.caboneftbe.services.MessagePublisher;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(API_PARAMS.API_VERSION)
@NoArgsConstructor(force = true)
@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class CheckHeathController {
    @Autowired
    private MessagePublisher messagePublisher;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok().body("OK");
    }

    @GetMapping("/send-message")
    public ResponseEntity<String> sendMessage(@RequestBody RabbitMqJsonRequest request) {
        messagePublisher.sendMessage(request);
        // mỗi khi call thì sẽ send message to rabbitMQ
        // send message xong thì message listener sẽ bắt (message) và xử lí
        return ResponseEntity.ok().body("Test send message - queued by rabbitMQ");
    }
}
