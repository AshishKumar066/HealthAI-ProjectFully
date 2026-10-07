package com.healthai.hospital_backend.security;

import com.healthai.hospital_backend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import com.healthai.hospital_backend.config.AppProperties;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
	private final SecretKey key;
	private final long expirationMs;

	public JwtService(AppProperties props) {
		this.key = Keys.hmacShaKeyFor(props.jwt().secret().getBytes(StandardCharsets.UTF_8));
		this.expirationMs = props.jwt().expirationMs();
	}

	public String generate(User u) {
		Date now = new Date();
		return Jwts.builder().subject(u.getEmail()).claim("role", u.getRole().name()).claim("name", u.getFullName())
				.issuedAt(now).expiration(new Date(now.getTime() + expirationMs)).signWith(key).compact();
	}

	public Claims parse(String token) {
		return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
	}
}
