package com.healthai.hospital_backend.repository;

import com.healthai.hospital_backend.entity.AppSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppSettingRepository extends JpaRepository<AppSetting, Long> {
}
