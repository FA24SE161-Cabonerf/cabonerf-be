package com.example.caboneftbe.exception;

import com.example.caboneftbe.enums.Constants;
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
                        .status(Constants.RESPONSE_STATUS_ERROR)
                        .message(message)
                        .build())
                .build();
    }
    public static CustomExceptions notFound(String message, Object data) {
        return CustomExceptions.builder()
                .status(HttpStatus.NOT_FOUND)
                .error(ErrorResponse.builder()
                        .status(Constants.RESPONSE_STATUS_ERROR)
                        .message(message)
                        .data(data)
                        .build())
                .build();
    }

    public static CustomExceptions badRequest(String message) {
        return CustomExceptions.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error(ErrorResponse.builder()
                        .status(Constants.RESPONSE_STATUS_ERROR)
                        .message(message)
                        .build())
                .build();
    }
    public static CustomExceptions badRequest(String message, Object data) {
        return CustomExceptions.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error(ErrorResponse.builder()
                        .status(Constants.RESPONSE_STATUS_ERROR)
                        .message(message)
                        .data(data)
                        .build())
                .build();
    }

    public static CustomExceptions unauthorized(String message) {
        return CustomExceptions.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .error(ErrorResponse.builder()
                        .status(Constants.RESPONSE_STATUS_ERROR)
                        .message(message)
                        .build())
                .build();
    }

    public static CustomExceptions unauthorized(String message, Object data) {
        return CustomExceptions.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .error(ErrorResponse.builder()
                        .status(Constants.RESPONSE_STATUS_ERROR)
                        .message(message)
                        .data(data)
                        .build())
                .build();
    }

    public static CustomExceptions validator(String message) {
        return CustomExceptions.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .error(ErrorResponse.builder()
                        .status(Constants.RESPONSE_STATUS_ERROR)
                        .message(message)
                        .build())
                .build();
    }

    public static CustomExceptions validator(String message, Object data) {
        return CustomExceptions.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .error(ErrorResponse.builder()
                        .status(Constants.RESPONSE_STATUS_ERROR)
                        .message(message)
                        .data(data)
                        .build())
                .build();
    }
}
