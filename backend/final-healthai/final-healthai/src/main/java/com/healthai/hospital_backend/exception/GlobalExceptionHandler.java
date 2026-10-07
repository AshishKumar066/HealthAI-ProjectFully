package com.healthai.hospital_backend.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private ResponseEntity<Map<String, Object>> body(HttpStatus status, String message, Map<String, String> fields) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("timestamp", LocalDateTime.now().toString());
		m.put("status", status.value());
		m.put("error", message);
		if (fields != null)
			m.put("fields", fields);
		return ResponseEntity.status(status).body(m);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, Object>> notFound(ResourceNotFoundException e) {
		return body(HttpStatus.NOT_FOUND, e.getMessage(), null);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Map<String, Object>> bad(BadRequestException e) {
		return body(HttpStatus.BAD_REQUEST, e.getMessage(), null);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> validation(MethodArgumentNotValidException e) {
		Map<String, String> fields = new LinkedHashMap<>();
		e.getBindingResult().getFieldErrors().forEach(f -> fields.put(f.getField(), f.getDefaultMessage()));
		return body(HttpStatus.BAD_REQUEST, "Validation failed", fields);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Map<String, Object>> creds(BadCredentialsException e) {
		return body(HttpStatus.UNAUTHORIZED, "Invalid email or password", null);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Map<String, Object>> denied(AccessDeniedException e) {
		return body(HttpStatus.FORBIDDEN, "You do not have permission to do this", null);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Map<String, Object>> integrity(DataIntegrityViolationException e) {
		return body(HttpStatus.CONFLICT,
				"Operation not allowed: this record is linked to other records or duplicates an existing one", null);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<Map<String, Object>> tooBig(MaxUploadSizeExceededException e) {
		return body(HttpStatus.PAYLOAD_TOO_LARGE, "File too large (max 15MB)", null);
	}

	@ExceptionHandler({ IllegalArgumentException.class,
			org.springframework.http.converter.HttpMessageNotReadableException.class })
	public ResponseEntity<Map<String, Object>> badInput(Exception e) {
		return body(HttpStatus.BAD_REQUEST, "Invalid request data: " + e.getMessage(), null);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> other(Exception e) throws Exception {
		// Let Spring's own 404/405/400 style errors keep their proper status code
		if (e instanceof org.springframework.web.ErrorResponse er)
			return body(HttpStatus.valueOf(er.getStatusCode().value()), e.getMessage(), null);
		e.printStackTrace();
		return body(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong: " + e.getMessage(), null);
	}
}
