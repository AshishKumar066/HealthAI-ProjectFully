package com.healthai.hospital_backend.service;

import com.healthai.hospital_backend.dto.Requests.*;
import com.healthai.hospital_backend.entity.Role;
import com.healthai.hospital_backend.entity.User;
import com.healthai.hospital_backend.exception.BadRequestException;
import com.healthai.hospital_backend.repository.UserRepository;
import com.healthai.hospital_backend.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AuthService {
	private final UserRepository users;
	private final PasswordEncoder encoder;
	private final JwtService jwt;

	public AuthService(UserRepository users, PasswordEncoder encoder, JwtService jwt) {
		this.users = users;
		this.encoder = encoder;
		this.jwt = jwt;
	}

	public Map<String, Object> login(Login r) {
		User u = users.findByEmailIgnoreCase(r.email()).orElseThrow(() -> new BadCredentialsException("bad"));
		if (!u.isActive() || !encoder.matches(r.password(), u.getPassword()))
			throw new BadCredentialsException("bad");
		return response(u);
	}

	/**
	 * Public sign-up always creates a PATIENT account. Staff accounts are created
	 * by an admin via /api/users.
	 */
	public Map<String, Object> register(Register r) {
		if (users.existsByEmailIgnoreCase(r.email()))
			throw new BadRequestException("An account with this email already exists");
		return response(create(r.fullName(), r.email(), r.password(), Role.PATIENT));
	}

	public User create(String name, String email, String rawPassword, Role role) {
		User u = new User();
		u.setFullName(name);
		u.setEmail(email.toLowerCase());
		u.setPassword(encoder.encode(rawPassword));
		u.setRole(role);
		return users.save(u);
	}

	public User byEmail(String email) {
		return users.findByEmailIgnoreCase(email).orElseThrow(() -> new BadCredentialsException("bad"));
	}

	private Map<String, Object> response(User u) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("token", jwt.generate(u));
		m.put("user", u);
		return m;
	}
}
