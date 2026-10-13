package com.healthai.hospital_backend.service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.healthai.hospital_backend.dto.Requests.Login;
import com.healthai.hospital_backend.dto.Requests.Register;
import com.healthai.hospital_backend.dto.Requests.ResendOtp;
import com.healthai.hospital_backend.dto.Requests.VerifyEmail;
import com.healthai.hospital_backend.entity.Role;
import com.healthai.hospital_backend.entity.User;
import com.healthai.hospital_backend.exception.BadRequestException;
import com.healthai.hospital_backend.repository.UserRepository;
import com.healthai.hospital_backend.security.JwtService;

@Service
public class AuthService {

	private final UserRepository users;
	private final PasswordEncoder encoder;
	private final JwtService jwt;
	private final EmailService emailService;

	private final SecureRandom random = new SecureRandom();

	private static final long OTP_VALIDITY_MILLIS = 10 * 60 * 1000;

	public AuthService(UserRepository users, PasswordEncoder encoder, JwtService jwt, EmailService emailService) {

		this.users = users;
		this.encoder = encoder;
		this.jwt = jwt;
		this.emailService = emailService;
	}

	// =====================================================
	// LOGIN
	// =====================================================

	public Map<String, Object> login(Login r) {

		User u = users.findByEmailIgnoreCase(r.email().trim()).orElseThrow(() -> new BadCredentialsException("bad"));

		if (!u.isActive()) {
			throw new BadRequestException("Your account is inactive");
		}

		if (!u.isEmailVerified()) {
			throw new BadRequestException("Please verify your email before signing in");
		}

		if (!encoder.matches(r.password(), u.getPassword())) {
			throw new BadCredentialsException("bad");
		}

		return response(u);
	}

	// =====================================================
	// REGISTER
	// =====================================================

	public Map<String, Object> register(Register r) {

		String email = r.email().trim().toLowerCase();

		validatePassword(r.password());

		if (!r.password().equals(r.confirmPassword())) {
			throw new BadRequestException("Passwords do not match");
		}

		if (users.existsByEmailIgnoreCase(email)) {
			throw new BadRequestException("An account with this email already exists");
		}

		User user = new User();

		user.setFullName(r.fullName().trim());
		user.setEmail(email);
		user.setPassword(encoder.encode(r.password()));

		// Public registration always creates patient
		user.setRole(Role.PATIENT);

		user.setActive(true);
		user.setEmailVerified(false);

		generateAndSendOtp(user);

		User saved = users.save(user);

		Map<String, Object> response = new LinkedHashMap<>();

		response.put("message", "Account created successfully. Please verify your email.");

		response.put("email", saved.getEmail());
		response.put("emailVerified", saved.isEmailVerified());

		return response;
	}

	// =====================================================
	// VERIFY EMAIL
	// =====================================================

	public Map<String, Object> verifyEmail(VerifyEmail request) {

		String email = request.email().trim().toLowerCase();

		User user = users.findByEmailIgnoreCase(email).orElseThrow(() -> new BadRequestException("Account not found"));

		if (user.isEmailVerified()) {

			return Map.of("message", "Email is already verified", "emailVerified", true);
		}

		if (user.getVerificationOtp() == null || user.getVerificationOtpExpiry() == null) {

			throw new BadRequestException("No active verification OTP. Please request a new OTP.");
		}

		if (Instant.now().toEpochMilli() > user.getVerificationOtpExpiry()) {

			throw new BadRequestException("OTP has expired. Please request a new OTP.");
		}

		if (!user.getVerificationOtp().equals(request.otp())) {

			throw new BadRequestException("Invalid OTP");
		}

		user.setEmailVerified(true);
		user.setVerificationOtp(null);
		user.setVerificationOtpExpiry(null);

		users.save(user);

		return Map.of("message", "Email verified successfully", "emailVerified", true);
	}

	// =====================================================
	// RESEND OTP
	// =====================================================

	public Map<String, Object> resendOtp(ResendOtp request) {

		String email = request.email().trim().toLowerCase();

		User user = users.findByEmailIgnoreCase(email).orElseThrow(() -> new BadRequestException("Account not found"));

		if (user.isEmailVerified()) {
			throw new BadRequestException("Email is already verified");
		}

		generateAndSendOtp(user);

		users.save(user);

		return Map.of("message", "A new verification OTP has been sent", "email", user.getEmail());
	}

	// =====================================================
	// CREATE STAFF / DEMO USERS
	// =====================================================

	public User create(String name, String email, String rawPassword, Role role) {

		User u = new User();

		u.setFullName(name);
		u.setEmail(email.toLowerCase());
		u.setPassword(encoder.encode(rawPassword));
		u.setRole(role);
		u.setActive(true);

		// Demo/admin/staff accounts are already trusted
		u.setEmailVerified(true);

		return users.save(u);
	}

	// =====================================================
	// GET USER
	// =====================================================

	public User byEmail(String email) {

		return users.findByEmailIgnoreCase(email).orElseThrow(() -> new BadCredentialsException("bad"));
	}

	// =====================================================
	// OTP GENERATION
	// =====================================================

	private void generateAndSendOtp(User user) {

		String otp = String.format("%06d", random.nextInt(1_000_000));

		user.setVerificationOtp(otp);

		user.setVerificationOtpExpiry(Instant.now().toEpochMilli() + OTP_VALIDITY_MILLIS);

		emailService.sendVerificationOtp(user.getEmail(), otp);
	}

	// =====================================================
	// PASSWORD VALIDATION
	// =====================================================

	private void validatePassword(String password) {

		if (password == null || password.length() < 6 || password.length() > 8) {

			throw new BadRequestException("Password must be 6 to 8 characters");
		}

		if (!password.matches(".*[A-Z].*")) {
			throw new BadRequestException("Password must contain at least one uppercase letter");
		}

		if (!password.matches(".*[a-z].*")) {
			throw new BadRequestException("Password must contain at least one lowercase letter");
		}

		if (!password.matches(".*\\d.*")) {
			throw new BadRequestException("Password must contain at least one number");
		}

		if (!password.matches(".*[^a-zA-Z0-9].*")) {
			throw new BadRequestException("Password must contain at least one special character");
		}
	}

	// =====================================================
	// JWT RESPONSE
	// =====================================================

	private Map<String, Object> response(User u) {

		Map<String, Object> m = new LinkedHashMap<>();

		m.put("token", jwt.generate(u));
		m.put("user", u);

		return m;
	}
}
