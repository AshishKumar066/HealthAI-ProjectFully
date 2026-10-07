package com.healthai.hospital_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String fullName;
	private String specialization;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "department_id")
	private Department department;
	private Integer experienceYears;
	private String qualification;
	private String phone;
	private String email;
	/** AVAILABLE, BUSY, ON_LEAVE */
	private String availability = "AVAILABLE";
	private BigDecimal consultationFee;
	private LocalTime workStart = LocalTime.of(9, 0);
	private LocalTime workEnd = LocalTime.of(17, 0);
	private Integer slotMinutes = 30;
}
