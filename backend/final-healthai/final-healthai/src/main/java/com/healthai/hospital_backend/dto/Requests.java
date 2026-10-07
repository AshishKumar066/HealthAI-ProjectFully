package com.healthai.hospital_backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/** All request bodies the React app sends. */
public final class Requests {
	private Requests() {
	}

	public record Login(@NotBlank @Email String email, @NotBlank String password) {
	}

	public record Register(@NotBlank String fullName, @NotBlank @Email String email,
			@NotBlank @Size(min = 6, message = "Password must be at least 6 characters") String password, String role) {
	}

	public record DoctorReq(@NotBlank(message = "Doctor name is required") String fullName, String specialization,
			Long departmentId, Integer experienceYears, String qualification, String phone, @Email String email,
			String availability, BigDecimal consultationFee, LocalTime workStart, LocalTime workEnd,
			Integer slotMinutes) {
	}

	public record AppointmentReq(@NotNull(message = "Patient is required") Long patientId,
			@NotNull(message = "Doctor is required") Long doctorId,
			@NotNull(message = "Date is required") LocalDate appointmentDate,
			@NotNull(message = "Time is required") LocalTime appointmentTime, String reason, String notes,
			String status) {
	}

	public record StatusReq(@NotBlank String status) {
	}

	public record RecordReq(@NotNull Long patientId, Long doctorId, @NotBlank String recordType, @NotBlank String title,
			String description, LocalDate recordDate) {
	}

	public record PrescriptionReq(@NotNull(message = "Patient is required") Long patientId,
			@NotNull(message = "Doctor is required") Long doctorId,
			@NotBlank(message = "Medicine is required") String medicine, String dosage, String frequency,
			Integer durationDays, String instructions, String status) {
	}

	public record ItemReq(String category, @NotBlank String description, @Min(1) Integer quantity,
			@NotNull @DecimalMin("0") BigDecimal unitPrice) {
	}

	public record InvoiceReq(@NotNull(message = "Patient is required") Long patientId,
			@NotEmpty(message = "Add at least one item") List<@Valid ItemReq> items, BigDecimal paidAmount,
			String paymentMethod) {
	}

	public record PaymentReq(@NotNull @DecimalMin(value = "0.01") BigDecimal amount, String method) {
	}

	public record EmergencyReq(@NotBlank String patientName, String phone, String description, String severity,
			String location, Long hospitalId) {
	}

	public record ChatReq(@NotBlank String message) {
	}
}
