package com.my.service.comms_service.exception;
import java.sql.SQLIntegrityConstraintViolationException;

import org.hibernate.QueryTimeoutException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.my.service.comms_service.dto.response.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationExceptions(
                        MethodArgumentNotValidException ex, WebRequest request) {

                // Get the first validation error message
                String errorMessage = ex.getBindingResult().getAllErrors().stream()
                                .map(error -> error.getDefaultMessage())
                                .findFirst()
                                .orElse("Validation failed");

                ErrorResponse errorResponse = ErrorResponse.of(
                                HttpStatus.BAD_REQUEST.value(),
                                "Validation Error",
                                errorMessage);

                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
                ErrorResponse errorResponse = ErrorResponse.of(
                                HttpStatus.NOT_FOUND.value(),
                                "Not Found",
                                ex.getMessage());
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                        WebRequest request) {
                Throwable cause = ex.getCause();
                String errorMessage = "Invalid JSON input";

                if (cause instanceof JsonMappingException jsonEx) {
                        String msg = jsonEx.getOriginalMessage();
                        if (msg != null && msg.contains("Decimals not allowed")) {
                                // Walk to the last path element to get the actual field
                                String fieldName = "unknown field";
                                if (!jsonEx.getPath().isEmpty()) {
                                        fieldName = jsonEx.getPath().get(jsonEx.getPath().size() - 1).getFieldName();
                                }
                                errorMessage = "Invalid value for '" + fieldName + "': decimals are not allowed";
                        } else if (msg != null) {
                                errorMessage = msg;
                        }
                } else if (cause instanceof JsonParseException parseEx) {
                        errorMessage = parseEx.getOriginalMessage();
                }

                ErrorResponse errorResponse = ErrorResponse.of(
                                HttpStatus.BAD_REQUEST.value(),
                                "Bad Request",
                                errorMessage);

                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
                ErrorResponse errorResponse = ErrorResponse.of(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Internal Server Error",
                                ex.getMessage());
                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(ResponseStatusException.class)
        public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex,
                        WebRequest request) {
                HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
                if (status == null) {
                        status = HttpStatus.INTERNAL_SERVER_ERROR;
                }

                ErrorResponse errorResponse = ErrorResponse.of(
                                status.value(),
                                status.getReasonPhrase(),
                                ex.getReason());

                return new ResponseEntity<>(errorResponse, status);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
                ErrorResponse errorResponse = ErrorResponse.of(
                                HttpStatus.BAD_REQUEST.value(),
                                "Bad Request",
                                ex.getMessage());
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        @ExceptionHandler(DataIntegrityViolationException.class)
public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
    ErrorResponse error = ErrorResponse.of(
        HttpStatus.BAD_REQUEST.value(),
        "Data integrity violation",
        ex.getMostSpecificCause().getMessage()
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
}
@ExceptionHandler(ConstraintViolationException.class)
public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
    ErrorResponse error = ErrorResponse.of(
        HttpStatus.BAD_REQUEST.value(),
        "Constraint violation",
        ex.getMessage()
    );
    return ResponseEntity.badRequest().body(error);
}
@ExceptionHandler(JpaSystemException.class)
public ResponseEntity<ErrorResponse> handleJpaSystem(JpaSystemException ex) {
    ErrorResponse error = ErrorResponse.of(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "JPA processing error",
        ex.getMessage()
    );
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
}
@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
public ResponseEntity<ErrorResponse> handleSqlConstraint(SQLIntegrityConstraintViolationException ex) {
    ErrorResponse error = ErrorResponse.of(
        HttpStatus.BAD_REQUEST.value(),
        "SQL constraint violation",
        ex.getMessage()
    );
    return ResponseEntity.badRequest().body(error);
}
@ExceptionHandler(QueryTimeoutException.class)
public ResponseEntity<ErrorResponse> handleQueryTimeout(QueryTimeoutException ex) {
    ErrorResponse error = ErrorResponse.of(
        HttpStatus.REQUEST_TIMEOUT.value(),
        "Database query timeout",
        ex.getMessage()
    );
    return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(error);
}


}
