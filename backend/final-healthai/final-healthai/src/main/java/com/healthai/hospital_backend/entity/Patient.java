package com.healthai.hospital_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message = "Full name is required")
	@Column(nullable = false)
	private String fullName;
	private LocalDate dateOfBirth;
	@Min(0)
	@Max(150)
	private Integer age;
	private String gender;
	@Pattern(regexp = "^[0-9+\\-\\s]{0,20}$", message = "Invalid phone number")
	private String phone;
	@Email(message = "Invalid email")
	private String email;
	private String bloodGroup;
	private String address;
	private String medicalCondition;
	private String emergencyContact;
	private String status = "Active";
	private LocalDateTime createdAt;

	@PrePersist
	void onCreate() {
		createdAt = LocalDateTime.now();
		if (status == null)
			status = "Active";
	}

	/** P001, P002 ... shown in the UI */
	public String getPatientCode() {
		return id == null ? null : String.format("P%03d", id);
	}
}
