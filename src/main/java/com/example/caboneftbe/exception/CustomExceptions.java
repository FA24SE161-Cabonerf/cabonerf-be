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
    CustomError error;

    public static CustomExceptions notFound(String message) {
        return CustomExceptions.builder()
                .status(HttpStatus.NOT_FOUND)
                .error(CustomError.builder()
                        .code(String.valueOf(HttpStatus.NOT_FOUND.value()))
                        .message(message)
                        .build())
                .build();
    }

    public static CustomExceptions badRequest(String message) {
        return CustomExceptions.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error(CustomError.builder()
                        .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .message(message)
                        .build())
                .build();
    }

    public static CustomExceptions unauthorized(String message) {
        return CustomExceptions.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .error(CustomError.builder()
                        .code(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                        .message(message)
                        .build())
                .build();
    }
}
