package com.healthai.hospital_backend.repository;

import com.healthai.hospital_backend.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
	boolean existsByNameIgnoreCase(String name);
}
