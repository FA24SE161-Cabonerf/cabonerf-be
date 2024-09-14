package com.example.caboneftbe.exception;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class GlobalExceptionHandler extends RuntimeException{
    HttpStatus status;
    String error;

    public static GlobalExceptionHandler notFound(String message) {
        return GlobalExceptionHandler.builder()
                .status(HttpStatus.NOT_FOUND)
                .error(message)
                .build();
    }

    public static GlobalExceptionHandler badRequest(String message) {
        return GlobalExceptionHandler.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error(message)
                .build();
    }

    public static GlobalExceptionHandler unauthorize(String message) {
        return GlobalExceptionHandler.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .error(message)
                .build();
    }
}
