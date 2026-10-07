package com.healthai.hospital_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "app_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppSetting {
	@Id
	private Long id = 1L;
	private String hospitalName = "HealthAI Hospital";
	private String contactEmail;
	private String contactPhone;
	private boolean emailNotifications = true;
	private boolean smsNotifications = false;
	private boolean darkMode = false;
	private String language = "en";
	private String timezone = "Asia/Kolkata";
	private String currency = "INR";
}
