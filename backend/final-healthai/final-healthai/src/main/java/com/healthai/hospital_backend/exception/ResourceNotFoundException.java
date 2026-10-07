package com.healthai.hospital_backend.exception;

public class ResourceNotFoundException extends RuntimeException {
	public ResourceNotFoundException(String what, Object id) {
		super(what + " not found with id: " + id);
	}
}
