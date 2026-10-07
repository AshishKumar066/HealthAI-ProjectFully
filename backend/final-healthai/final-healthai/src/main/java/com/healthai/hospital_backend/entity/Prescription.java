package com.healthai.hospital_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "prescriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {
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
	private String medicine;
	private String dosage;
	private String frequency;
	private Integer durationDays;
	@Column(length = 1000)
	private String instructions;
	private LocalDate prescribedDate;
	/** ACTIVE, COMPLETED, CANCELLED */
	private String status = "ACTIVE";

	@PrePersist
	void onCreate() {
		if (prescribedDate == null)
			prescribedDate = LocalDate.now();
	}
}
