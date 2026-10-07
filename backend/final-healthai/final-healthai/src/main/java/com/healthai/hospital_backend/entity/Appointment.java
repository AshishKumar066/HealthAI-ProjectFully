package com.healthai.hospital_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "patient_id")
	private Patient patient;
	@ManyToOne(optional = false)
	@JoinColumn(name = "doctor_id")
	private Doctor doctor;
	@Column(nullable = false)
	private LocalDate appointmentDate;
	@Column(nullable = false)
	private LocalTime appointmentTime;
	private String reason;
	/** SCHEDULED, CONFIRMED, COMPLETED, CANCELLED */
	private String status = "SCHEDULED";
	private String notes;
	private LocalDateTime createdAt;

	@PrePersist
	void onCreate() {
		createdAt = LocalDateTime.now();
	}
}
