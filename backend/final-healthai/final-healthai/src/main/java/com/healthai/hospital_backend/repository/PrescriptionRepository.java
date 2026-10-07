package com.healthai.hospital_backend.repository;

import com.healthai.hospital_backend.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
	@Query("select p from Prescription p where (:patientId is null or p.patient.id = :patientId) "
			+ "and (:doctorId is null or p.doctor.id = :doctorId) "
			+ "and (:q is null or :q = '' or lower(p.medicine) like lower(concat('%', :q, '%')) "
			+ "or lower(p.patient.fullName) like lower(concat('%', :q, '%'))) order by p.prescribedDate desc, p.id desc")
	List<Prescription> search(@Param("patientId") Long patientId, @Param("doctorId") Long doctorId,
			@Param("q") String q);

	List<Prescription> findByMedicineContainingIgnoreCaseOrPatientFullNameContainingIgnoreCase(String m, String n);
}
