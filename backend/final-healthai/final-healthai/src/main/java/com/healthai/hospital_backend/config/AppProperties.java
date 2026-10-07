package com.healthai.hospital_backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

/**
 * Type-safe binding of every "app.*" key in application.properties
 * (@ConfigurationProperties).
 */
@ConfigurationProperties(prefix = "app")
public record AppProperties(Jwt jwt, Cors cors, String uploadDir, boolean seedDemoData, Channels channels) {
	public record Jwt(String secret, long expirationMs) {
	}

	public record Cors(List<String> allowedOrigins) {
	}

	public record Channels(boolean emailEnabled) {
	}
}
