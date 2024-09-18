package com.example.caboneftbe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, ErrorDetail> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), ErrorDetail.builder()
                    .value(fieldError.getRejectedValue())
                    .msg(fieldError.getDefaultMessage())
                    .path(fieldError.getField())
                    .build());
        });

        ErrorResponse response = ErrorResponse.builder()
                .status("Failed")
                .message("Validation failed")
                .data(errors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        ErrorResponse response = ErrorResponse.builder()
                .status("Error")
                .message("Error")
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomExceptions.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(CustomExceptions ex){
        return new ResponseEntity<>(ex.getError(), ex.getStatus());
    }
}
