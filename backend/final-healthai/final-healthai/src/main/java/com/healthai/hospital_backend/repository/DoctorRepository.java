package com.healthai.hospital_backend.repository;

import com.healthai.hospital_backend.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
	@Query("select d from Doctor d where (:q is null or :q = '' or lower(d.fullName) like lower(concat('%', :q, '%')) "
			+ "or lower(d.specialization) like lower(concat('%', :q, '%'))) "
			+ "and (:departmentId is null or d.department.id = :departmentId) "
			+ "and (:availability is null or :availability = '' or upper(d.availability) = upper(:availability)) order by d.fullName")
	List<Doctor> search(@Param("q") String q, @Param("departmentId") Long departmentId,
			@Param("availability") String availability);

	long countByAvailabilityIgnoreCase(String availability);

	long countByDepartmentId(Long departmentId);
}
