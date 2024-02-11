package org.learning.microservices.web;

import org.learning.microservices.domain.ErrorResponse;
import org.learning.microservices.domain.ValidationErrorResponse;
import org.learning.microservices.exception.DataNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class CustomHandlerExceptionResolver extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = createValidationErrorResponse(ex, request, status);
        return createResponseEntity(errorResponse, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Object> handleDataNotFoundException(RuntimeException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse errorResponse = createErrorResponse(ex, request, status);
        return createResponseEntity(errorResponse, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = createErrorResponse(ex, request, status);
        return createResponseEntity(errorResponse, new HttpHeaders(), status, request);
    }

    private ErrorResponse createErrorResponse(Exception ex, WebRequest request, HttpStatus status) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(ex.getMessage())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();
    }

    private ValidationErrorResponse.ViolationFieldError createViolationFieldError(FieldError error) {
        return ValidationErrorResponse.ViolationFieldError.builder()
                .field(error.getField())
                .message(error.getDefaultMessage())
                .build();
    }

    private ErrorResponse createValidationErrorResponse(
            MethodArgumentNotValidException ex, WebRequest request, HttpStatus status) {

        var violationFieldErrors = ex.getAllErrors().stream()
                .map(error -> (FieldError) error)
                .map(this::createViolationFieldError)
                .toList();

        return ValidationErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message("Request validation error")
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .violations(violationFieldErrors)
                .build();
    }

}
