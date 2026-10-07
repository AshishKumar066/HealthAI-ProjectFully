package com.healthai.hospital_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "emergency_cases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyCase {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String patientName;
	private String phone;
	@Column(length = 1000)
	private String description;
	/** LOW, MEDIUM, HIGH, CRITICAL */
	private String severity = "HIGH";
	private String location;
	/** OPEN, DISPATCHED, ADMITTED, RESOLVED */
	private String status = "OPEN";
	@ManyToOne
	@JoinColumn(name = "hospital_id")
	private Hospital hospital;
	private LocalDateTime createdAt;

	@PrePersist
	void onCreate() {
		createdAt = LocalDateTime.now();
	}
}
