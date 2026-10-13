package com.healthai.hospital_backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * All request bodies the React app sends.
 */
public final class Requests {

	private Requests() {
	}

	// =========================
	// LOGIN
	// =========================

	public record Login(
			@NotBlank(message = "Email is required") @Email(message = "Enter a valid email") String email,

			@NotBlank(message = "Password is required") String password) {
	}

	// =========================
	// REGISTER
	// =========================

	public record Register(

			@NotBlank(message = "Full name is required") @Size(max = 100, message = "Name is too long") String fullName,

			@NotBlank(message = "Email is required") @Email(message = "Enter a valid email") String email,

			@NotBlank(message = "Password is required") @Size(min = 6, max = 8, message = "Password must be 6 to 8 characters") String password,

			@NotBlank(message = "Confirm password is required") String confirmPassword,

			String role) {
	}

	// =========================
	// VERIFY EMAIL
	// =========================

	public record VerifyEmail(

			@NotBlank(message = "Email is required") @Email(message = "Enter a valid email") String email,

			@NotBlank(message = "OTP is required") @Pattern(regexp = "\\d{6}", message = "OTP must be 6 digits") String otp) {
	}

	// =========================
	// RESEND OTP
	// =========================

	public record ResendOtp(

			@NotBlank(message = "Email is required") @Email(message = "Enter a valid email") String email) {
	}

	// =========================
	// DOCTOR
	// =========================

	public record DoctorReq(
			@NotBlank(message = "Doctor name is required") String fullName,
			String specialization,
			Long departmentId,
			Integer experienceYears,
			String qualification,
			String phone,
			@Email String email,
			String availability,
			BigDecimal consultationFee,
			LocalTime workStart,
			LocalTime workEnd,
			Integer slotMinutes) {
	}

	// =========================
	// APPOINTMENT
	// =========================

	public record AppointmentReq(
			@NotNull(message = "Patient is required") Long patientId,
			@NotNull(message = "Doctor is required") Long doctorId,
			@NotNull(message = "Date is required") LocalDate appointmentDate,
			@NotNull(message = "Time is required") LocalTime appointmentTime,
			String reason,
			String notes,
			String status) {
	}

	public record StatusReq(
			@NotBlank(message = "Status is required") String status) {
	}

	// =========================
	// MEDICAL RECORD
	// =========================

	public record RecordReq(
			@NotNull Long patientId,
			Long doctorId,
			@NotBlank String recordType,
			@NotBlank String title,
			String description,
			LocalDate recordDate) {
	}

	// =========================
	// PRESCRIPTION
	// =========================

	public record PrescriptionReq(
			@NotNull(message = "Patient is required") Long patientId,
			@NotNull(message = "Doctor is required") Long doctorId,
			@NotBlank(message = "Medicine is required") String medicine,
			String dosage,
			String frequency,
			Integer durationDays,
			String instructions,
			String status) {
	}

	// =========================
	// BILLING
	// =========================

	public record ItemReq(
			String category,
			@NotBlank String description,
			@Min(1) Integer quantity,
			@NotNull @DecimalMin("0") BigDecimal unitPrice) {
	}

	public record InvoiceReq(
			@NotNull(message = "Patient is required") Long patientId,
			@NotEmpty(message = "Add at least one item") List<@Valid ItemReq> items,
			BigDecimal paidAmount,
			String paymentMethod) {
	}

	public record PaymentReq(
			@NotNull @DecimalMin(value = "0.01") BigDecimal amount,
			String method) {
	}

	// =========================
	// EMERGENCY
	// =========================

	public record EmergencyReq(
			@NotBlank String patientName,
			String phone,
			String description,
			String severity,
			String location,
			Long hospitalId) {
	}

	// =========================
	// AI CHAT
	// =========================

	public record ChatReq(
			@NotBlank String message) {
	}
}
