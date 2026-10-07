package com.healthai.hospital_backend.repository;

import com.healthai.hospital_backend.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
	@Query("select r from MedicalRecord r where (:patientId is null or r.patient.id = :patientId) "
			+ "and (:type is null or :type = '' or lower(r.recordType) = lower(:type)) "
			+ "and (:q is null or :q = '' or lower(r.title) like lower(concat('%', :q, '%')) "
			+ "or lower(r.patient.fullName) like lower(concat('%', :q, '%'))) order by r.recordDate desc, r.id desc")
	List<MedicalRecord> search(@Param("patientId") Long patientId, @Param("type") String type, @Param("q") String q);
}
