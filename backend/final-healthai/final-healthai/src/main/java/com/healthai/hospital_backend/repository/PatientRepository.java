package com.healthai.hospital_backend.repository;

import com.healthai.hospital_backend.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
	@Query("select p from Patient p where (:q is null or :q = '' or lower(p.fullName) like lower(concat('%', :q, '%')) "
			+ "or lower(p.phone) like lower(concat('%', :q, '%')) or lower(p.email) like lower(concat('%', :q, '%')) "
			+ "or lower(p.medicalCondition) like lower(concat('%', :q, '%'))) "
			+ "and (:status is null or :status = '' or lower(p.status) = lower(:status)) order by p.id desc")
	List<Patient> search(@Param("q") String q, @Param("status") String status);

	long countByStatusIgnoreCase(String status);
}
