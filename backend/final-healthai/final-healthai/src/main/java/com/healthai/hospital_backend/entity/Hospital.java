package com.healthai.hospital_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "hospitals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Hospital {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	private String name;
	private String address;
	private String city;
	private String phone;
	private Double latitude;
	private Double longitude;
	private boolean emergencyAvailable = true;
	private Integer totalBeds = 0;
	private Integer availableBeds = 0;
	private Integer icuBeds = 0;
	private Integer availableIcuBeds = 0;
	@Column(length = 500)
	private String services;
}
