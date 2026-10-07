package com.healthai.hospital_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(optional = false)
	@JoinColumn(name = "patient_id")
	private Patient patient;
	@ManyToOne
	@JoinColumn(name = "doctor_id")
	private Doctor doctor;
	/** Blood Test, MRI, X-Ray, Prescription, Discharge Summary, Other */
	private String recordType;
	private String title;
	@Column(length = 2000)
	private String description;
	private LocalDate recordDate;
	private String fileName;
	@JsonIgnore
	private String storedFileName;
	private String contentType;
	private Long fileSize;
	private LocalDateTime createdAt;

	@PrePersist
	void onCreate() {
		createdAt = LocalDateTime.now();
		if (recordDate == null)
			recordDate = LocalDate.now();
	}

	public boolean isHasFile() {
		return storedFileName != null;
	}
}
