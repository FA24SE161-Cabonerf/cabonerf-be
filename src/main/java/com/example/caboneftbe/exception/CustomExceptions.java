package com.example.caboneftbe.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class CustomExceptions extends RuntimeException{
    HttpStatus status;
    ErrorResponse error;

    public static CustomExceptions notFound(String message) {
        return CustomExceptions.builder()
                .status(HttpStatus.NOT_FOUND)
                .error(ErrorResponse.builder()
                        .status(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .message(message)
                        .build())
                .build();
    }
    public static CustomExceptions notFound(String message, Object data) {
        return CustomExceptions.builder()
                .status(HttpStatus.NOT_FOUND)
                .error(ErrorResponse.builder()
                        .status(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .message(message)
                        .data(data)
                        .build())
                .build();
    }

    public static CustomExceptions badRequest(String message) {
        return CustomExceptions.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error(ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .message(message)
                        .build())
                .build();
    }
    public static CustomExceptions badRequest(String message, Object data) {
        return CustomExceptions.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error(ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .message(message)
                        .data(data)
                        .build())
                .build();
    }

    public static CustomExceptions unauthorized(String message) {
        return CustomExceptions.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .error(ErrorResponse.builder()
                        .status(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                        .message(message)
                        .build())
                .build();
    }

    public static CustomExceptions unauthorized(String message, Object data) {
        return CustomExceptions.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .error(ErrorResponse.builder()
                        .status(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                        .message(message)
                        .data(data)
                        .build())
                .build();
    }
}
