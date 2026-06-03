package com.donaton.auth.exception;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException exception) {
		Map<String, String> errors = new HashMap<>();
		for (FieldError error : exception.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
		return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed", errors);
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<Map<String, Object>> handleConflict(ConflictException exception) {
		return buildResponse(HttpStatus.CONFLICT, exception.getMessage(), null);
	}

	@ExceptionHandler({AuthenticationFailedException.class, InvalidTokenException.class})
	public ResponseEntity<Map<String, Object>> handleUnauthorized(RuntimeException exception) {
		return buildResponse(HttpStatus.UNAUTHORIZED, exception.getMessage(), null);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException exception) {
		return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), null);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Map<String, Object>> handleForbidden(AccessDeniedException exception) {
		return buildResponse(HttpStatus.FORBIDDEN, exception.getMessage(), null);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGeneric(Exception exception) {
		return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error", null);
	}

	private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message, Object details) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", OffsetDateTime.now());
		body.put("status", status.value());
		body.put("message", message);
		if (details != null) {
			body.put("details", details);
		}
		return ResponseEntity.status(status).body(body);
	}
}
