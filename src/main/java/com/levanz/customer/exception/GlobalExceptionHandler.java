package com.levanz.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String,String>> handleNotFound(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                             .body(Map.of("error", ex.getReason()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleBindException(
            MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<Map<String,String>> errors = result.getFieldErrors().stream()
            .map(fe -> Map.of(
                    "field", fe.getField(),
                    "message", fe.getDefaultMessage()))
            .collect(Collectors.toList());
        return ResponseEntity.badRequest()
            .body(Map.of(
                "error", "Validation failed",
                "details", errors
            ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,String>> handleAll(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(Map.of("error", "Internal server error"));
    }
}
