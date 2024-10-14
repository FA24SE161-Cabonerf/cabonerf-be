package com.example.caboneftbe.controller;

import com.example.caboneftbe.enums.API_PARAMS;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(API_PARAMS.API_VERSION)
@NoArgsConstructor(force = true)
@RestController
@CrossOrigin(origins = "*")
public class CheckHeathController {
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok().body("OK");
    }
}
