package com.healthai.hospital_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Value("${app.email.dev-mode:true}")
	private boolean devMode;

	/**
	 * Sends verification OTP.
	 *
	 * Development mode:
	 * OTP is printed in the Spring Boot console.
	 *
	 * Production:
	 * This service can later be connected with SMTP.
	 */
	public void sendVerificationOtp(String email, String otp) {

		if (devMode) {
			System.out.println();
			System.out.println("==============================================");
			System.out.println("          HEALTHAI EMAIL VERIFICATION");
			System.out.println("==============================================");
			System.out.println("Email : " + email);
			System.out.println("OTP   : " + otp);
			System.out.println("Valid : 10 minutes");
			System.out.println("==============================================");
			System.out.println();

			return;
		}

		/*
		 * SMTP email implementation can be added here.
		 */
		System.out.println("Verification email sent to: " + email);
	}
}