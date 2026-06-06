package com.donaton.bff.exception;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BffExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException exception) {
		Map<String, String> details = new LinkedHashMap<>();
		for (FieldError error : exception.getBindingResult().getFieldErrors()) {
			details.put(error.getField(), error.getDefaultMessage());
		}
		return build(HttpStatus.BAD_REQUEST, "Validation failed", details);
	}

	@ExceptionHandler(UpstreamServiceException.class)
	public ResponseEntity<Map<String, Object>> handleUpstream(UpstreamServiceException exception) {
		HttpStatus status = HttpStatus.resolve(exception.getStatus().value());
		if (status == null) {
			status = HttpStatus.BAD_GATEWAY;
		}
		return build(status, exception.getUpstreamMessage(), null);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<Map<String, Object>> handleUnauthorized(UnauthorizedException exception) {
		return build(HttpStatus.UNAUTHORIZED, exception.getMessage(), null);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGeneric(Exception exception) {
		return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error", null);
	}

	private static ResponseEntity<Map<String, Object>> build(
		HttpStatus status,
		String message,
		Object details
	) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", OffsetDateTime.now().toString());
		body.put("status", status.value());
		body.put("message", message);
		if (details != null) {
			body.put("details", details);
		}
		return ResponseEntity.status(status).body(body);
	}
}
