package com.example.cabonerfbe.exception;

import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.response.ResponseObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class Global exception handler.
 *
 * @author SonPHH.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * Handle validation exception method.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        ErrorResponse response = ErrorResponse.builder()
                .status("Error")
                .message("Validation failed")
                .data(errors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Handle global exception method.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        ErrorResponse response = ErrorResponse.builder()
                .status("Error")
                .message(ex.getMessage())
                .build();
        log.error(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle global exception method.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler(CustomExceptions.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(CustomExceptions ex) {
        return new ResponseEntity<>(ex.getError(), ex.getStatus());
    }

    /**
     * Handle missing header method.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ResponseObject> handleMissingHeader(MissingRequestHeaderException ex) {
        return ResponseEntity.status(401).body(
                new ResponseObject(Constants.RESPONSE_STATUS_ERROR, "Error", Map.of("Authorization", "Unauthorized access")));
    }

    /**
     * Handle validation body method.
     *
     * @param ex the ex
     * @return the response entity
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleValidationBody(HttpMessageNotReadableException ex) {
        // Log the detailed error for debugging
        log.error("JSON parse error: ", ex);

        // Extract the specific error message
        String errorMessage = ex.getCause() != null ? ex.getCause().getMessage() : "Invalid request body";
        // Dynamically detect the field names from the error message (improved logic)
        Map<String, String> errors = extractFieldNamesFromMessage(errorMessage);

        // Prepare the error response
        ErrorResponse response = ErrorResponse.builder()
                .status("Error")
                .message("Validation failed")
                .data(errors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Helper method to extract all field names from the error message
    private Map<String, String> extractFieldNamesFromMessage(String errorMessage) {
        Map<String, String> errors = new HashMap<>();

        // Look for all occurrences of the reference chain pattern (multiple fields)
        Pattern pattern = Pattern.compile("through reference chain: .*?\\[\"(.*?)\"]");
        Matcher matcher = pattern.matcher(errorMessage);

        // Add all matching field names to the error map
        while (matcher.find()) {
            String fieldName = matcher.group(1); // Extract field name
            errors.put(fieldName, "Invalid value for field: " + fieldName);
        }

        // If no fields were found, add a default error
        if (errors.isEmpty()) {
            errors.put("UnknownField", "Invalid request body");
        }

        return errors;
    }

}

