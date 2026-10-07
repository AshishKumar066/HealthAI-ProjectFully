package com.healthai.hospital_backend.repository;

import com.healthai.hospital_backend.entity.EmergencyCase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmergencyCaseRepository extends JpaRepository<EmergencyCase, Long> {
	List<EmergencyCase> findAllByOrderByIdDesc();

	long countByStatusNot(String status);
}
