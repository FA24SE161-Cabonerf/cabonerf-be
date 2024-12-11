package com.example.cabonerfbe.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * The class Error detail.
 *
 * @author SonPHH.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorDetail {
    String message;
    String cause;
    String stackTrace;
}

