package com.healthai.hospital_backend.controller;

import com.healthai.hospital_backend.dto.Requests.*;
import com.healthai.hospital_backend.entity.Role;
import com.healthai.hospital_backend.entity.User;
import com.healthai.hospital_backend.exception.BadRequestException;
import com.healthai.hospital_backend.repository.UserRepository;
import com.healthai.hospital_backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {
	private final AuthService auth;
	private final UserRepository users;

	public AuthController(AuthService auth, UserRepository users) {
		this.auth = auth;
		this.users = users;
	}

	@PostMapping("/auth/login")
	public Map<String, Object> login(@Valid @RequestBody Login r) {
		return auth.login(r);
	}

	@PostMapping("/auth/register")
	@ResponseStatus(HttpStatus.CREATED)
	public Map<String, Object> register(@Valid @RequestBody Register r) {
		return auth.register(r);
	}

	@GetMapping("/auth/me")
	public User me(Authentication a) {
		return auth.byEmail(a.getName());
	}

	@GetMapping("/health")
	public Map<String, String> health() {
		return Map.of("status", "UP");
	}

	// ---- admin: staff accounts ----
	@GetMapping("/users")
	public List<User> users(Authentication a) {
		requireAdmin(a);
		return users.findAll();
	}

	@PostMapping("/users")
	@ResponseStatus(HttpStatus.CREATED)
	public User createUser(@Valid @RequestBody Register r, Authentication a) {
		requireAdmin(a);
		if (users.existsByEmailIgnoreCase(r.email()))
			throw new BadRequestException("An account with this email already exists");
		Role role = r.role() == null ? Role.RECEPTIONIST : Role.valueOf(r.role().toUpperCase());
		return auth.create(r.fullName(), r.email(), r.password(), role);
	}

	private void requireAdmin(Authentication a) {
		if (a.getAuthorities().stream().noneMatch(g -> g.getAuthority().equals("ROLE_ADMIN")))
			throw new org.springframework.security.access.AccessDeniedException("Admin only");
	}
}
