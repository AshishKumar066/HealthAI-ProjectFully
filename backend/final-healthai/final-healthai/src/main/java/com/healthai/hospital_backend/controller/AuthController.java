package com.healthai.hospital_backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.healthai.hospital_backend.dto.Requests.Login;
import com.healthai.hospital_backend.dto.Requests.Register;
import com.healthai.hospital_backend.dto.Requests.ResendOtp;
import com.healthai.hospital_backend.dto.Requests.VerifyEmail;
import com.healthai.hospital_backend.entity.Role;
import com.healthai.hospital_backend.entity.User;
import com.healthai.hospital_backend.exception.BadRequestException;
import com.healthai.hospital_backend.repository.UserRepository;
import com.healthai.hospital_backend.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {

	private final AuthService auth;
	private final UserRepository users;

	public AuthController(AuthService auth, UserRepository users) {

		this.auth = auth;
		this.users = users;
	}

	// =====================================================
	// LOGIN
	// =====================================================

	@PostMapping("/auth/login")
	public Map<String, Object> login(@Valid @RequestBody Login request) {

		return auth.login(request);
	}

	// =====================================================
	// REGISTER
	// =====================================================

	@PostMapping("/auth/register")
	@ResponseStatus(HttpStatus.CREATED)
	public Map<String, Object> register(@Valid @RequestBody Register request) {

		return auth.register(request);
	}

	// =====================================================
	// VERIFY EMAIL
	// =====================================================

	@PostMapping("/auth/verify-email")
	public Map<String, Object> verifyEmail(@Valid @RequestBody VerifyEmail request) {

		return auth.verifyEmail(request);
	}

	// =====================================================
	// RESEND OTP
	// =====================================================

	@PostMapping("/auth/resend-otp")
	public Map<String, Object> resendOtp(@Valid @RequestBody ResendOtp request) {

		return auth.resendOtp(request);
	}

	// =====================================================
	// CURRENT USER
	// =====================================================

	@GetMapping("/auth/me")
	public User me(Authentication authentication) {

		return auth.byEmail(authentication.getName());
	}

	// =====================================================
	// HEALTH
	// =====================================================

	@GetMapping("/health")
	public Map<String, String> health() {

		return Map.of("status", "UP");
	}

	// =====================================================
	// ADMIN USERS
	// =====================================================

	@GetMapping("/users")
	public List<User> users(Authentication authentication) {

		requireAdmin(authentication);

		return users.findAll();
	}

	@PostMapping("/users")
	@ResponseStatus(HttpStatus.CREATED)
	public User createUser(@Valid @RequestBody Register request, Authentication authentication) {

		requireAdmin(authentication);

		if (users.existsByEmailIgnoreCase(request.email())) {

			throw new BadRequestException("An account with this email already exists");
		}

		Role role = request.role() == null ? Role.RECEPTIONIST : Role.valueOf(request.role().toUpperCase());

		return auth.create(request.fullName(), request.email(), request.password(), role);
	}

	// =====================================================
	// ADMIN CHECK
	// =====================================================

	private void requireAdmin(Authentication authentication) {

		if (authentication == null
				|| authentication.getAuthorities()
						.stream()
						.noneMatch(
								g -> g.getAuthority()
										.equals("ROLE_ADMIN"))) {

			throw new org.springframework.security.access.AccessDeniedException(
					"Admin only");
		}
	}
}
