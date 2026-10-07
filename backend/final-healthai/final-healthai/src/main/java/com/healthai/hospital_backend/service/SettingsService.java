package com.healthai.hospital_backend.service;

import com.healthai.hospital_backend.entity.AppSetting;
import com.healthai.hospital_backend.repository.AppSettingRepository;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {
	private final AppSettingRepository repo;

	public SettingsService(AppSettingRepository repo) {
		this.repo = repo;
	}

	public AppSetting get() {
		return repo.findById(1L).orElseGet(() -> repo.save(new AppSetting()));
	}

	public AppSetting update(AppSetting in) {
		AppSetting s = get();
		s.setHospitalName(in.getHospitalName());
		s.setContactEmail(in.getContactEmail());
		s.setContactPhone(in.getContactPhone());
		s.setEmailNotifications(in.isEmailNotifications());
		s.setSmsNotifications(in.isSmsNotifications());
		s.setDarkMode(in.isDarkMode());
		s.setLanguage(in.getLanguage());
		s.setTimezone(in.getTimezone());
		s.setCurrency(in.getCurrency());
		return repo.save(s);
	}
}
