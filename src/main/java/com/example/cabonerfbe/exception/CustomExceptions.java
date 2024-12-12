package com.example.cabonerfbe.exception;

import com.example.cabonerfbe.enums.Constants;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.util.Collections;


/**
 * The class Custom exceptions.
 *
 * @author SonPHH.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Builder
public class CustomExceptions extends RuntimeException {
    HttpStatus status;
    ErrorResponse error;

    /**
     * Not found method.
     *
     * @param message the message
     * @return the custom exceptions
     */
    public static CustomExceptions notFound(String message) {
        return CustomExceptions.builder()
                .status(HttpStatus.NOT_FOUND)
                .error(ErrorResponse.builder()
                        .status(Constants.RESPONSE_STATUS_ERROR)
                        .message(message)
                        .data(Collections.EMPTY_LIST)
                        .build())
                .build();
    }

    /**
     * Not found method.
     *
     * @param message the message
     * @param data    the data
     * @return the custom exceptions
     */
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

    /**
     * Bad request method.
     *
     * @param message the message
     * @return the custom exceptions
     */
    public static CustomExceptions badRequest(String message) {
        return CustomExceptions.builder()
                .status(HttpStatus.BAD_REQUEST)
                .error(ErrorResponse.builder()
                        .status(Constants.RESPONSE_STATUS_ERROR)
                        .message(message)
                        .data(Collections.EMPTY_LIST)
                        .build())
                .build();
    }

    /**
     * Bad request method.
     *
     * @param message the message
     * @param data    the data
     * @return the custom exceptions
     */
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

    /**
     * Unauthorized method.
     *
     * @param message the message
     * @return the custom exceptions
     */
    public static CustomExceptions unauthorized(String message) {
        return CustomExceptions.builder()
                .status(HttpStatus.UNAUTHORIZED)
                .error(ErrorResponse.builder()
                        .status(Constants.RESPONSE_STATUS_ERROR)
                        .message(message)
                        .data(Collections.EMPTY_LIST)
                        .build())
                .build();
    }

    /**
     * Unauthorized method.
     *
     * @param message the message
     * @param data    the data
     * @return the custom exceptions
     */
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

    /**
     * Validator method.
     *
     * @param message the message
     * @return the custom exceptions
     */
    public static CustomExceptions validator(String message) {
        return CustomExceptions.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .error(ErrorResponse.builder()
                        .status(Constants.RESPONSE_STATUS_ERROR)
                        .message(message)
                        .data(Collections.EMPTY_LIST)
                        .build())
                .build();
    }

    /**
     * Validator method.
     *
     * @param message the message
     * @param data    the data
     * @return the custom exceptions
     */
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

    /**
     * No content method.
     *
     * @param message the message
     * @return the custom exceptions
     */
    public static CustomExceptions noContent(String message) {
        return CustomExceptions.builder()
                .status(HttpStatus.NO_CONTENT)
                .error(ErrorResponse.builder()
                        .status(Constants.RESPONSE_STATUS_ERROR)
                        .message(message)
                        .data(Collections.EMPTY_LIST)
                        .build())
                .build();
    }

    /**
     * No content method.
     *
     * @param message the message
     * @param data    the data
     * @return the custom exceptions
     */
    public static CustomExceptions noContent(String message, Object data) {
        return CustomExceptions.builder()
                .status(HttpStatus.NO_CONTENT)
                .error(ErrorResponse.builder()
                        .status(Constants.RESPONSE_STATUS_ERROR)
                        .message(message)
                        .data(data)
                        .build())
                .build();
    }
}
