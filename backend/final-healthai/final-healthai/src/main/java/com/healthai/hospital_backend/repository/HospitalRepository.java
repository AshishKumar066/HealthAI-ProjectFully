package com.healthai.hospital_backend.repository;

import com.healthai.hospital_backend.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
	List<Hospital> findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(String name, String city);
}
