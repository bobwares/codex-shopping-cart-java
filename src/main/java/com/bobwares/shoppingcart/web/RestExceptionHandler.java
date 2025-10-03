/**
 * App: Shopping Cart API
 * Package: com.bobwares.shoppingcart.web
 * File: RestExceptionHandler.java
 * Version: 0.1.0
 * Turns: 2
 * Author: gpt-5-codex
 * Date: 2025-10-03T00:25:40Z
 * Exports: RestExceptionHandler
 * Description: Translates common exceptions into structured HTTP error responses for the REST API.
 */
package com.bobwares.shoppingcart.web;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handles validation and domain exceptions, ensuring consistent API error payloads.
 */
@RestControllerAdvice
public class RestExceptionHandler {

  /**
   * Handles cases where a requested entity cannot be located.
   *
   * @param ex exception thrown by service layer
   * @return 404 response payload
   */
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorResponse(ex.getMessage(), Map.of(), Instant.now()));
  }

  /**
   * Handles invalid arguments, typically resulting from domain constraint violations.
   *
   * @param ex exception thrown when user action conflicts with invariants
   * @return 409 response payload
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ErrorResponse(ex.getMessage(), Map.of(), Instant.now()));
  }

  /**
   * Handles bean validation errors originating from controller method parameters.
   *
   * @param ex exception containing validation failures
   * @return 422 response payload with detailed field errors
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
      errors.put(fieldError.getField(), fieldError.getDefaultMessage());
    }
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
        .body(new ErrorResponse("Validation failed", errors, Instant.now()));
  }

  /**
   * Handles validation errors thrown outside of method argument binding, such as service-level validations.
   *
   * @param ex constraint violation exception
   * @return 422 response payload with violation details
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
    Map<String, String> errors = new HashMap<>();
    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      errors.put(violation.getPropertyPath().toString(), violation.getMessage());
    }
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
        .body(new ErrorResponse("Validation failed", errors, Instant.now()));
  }

  private record ErrorResponse(String message, Map<String, String> errors, Instant timestamp) {
  }
}
