package com.example.caboneftbe.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomExceptions.class)
    public ResponseEntity<CustomError> handleGlobalException(CustomExceptions ex){
        return new ResponseEntity<>(ex.getError(), ex.getStatus());
    }
}
