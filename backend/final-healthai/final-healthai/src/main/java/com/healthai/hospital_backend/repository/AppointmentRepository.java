package com.healthai.hospital_backend.repository;

import com.healthai.hospital_backend.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
	@Query("select a from Appointment a where (:patientId is null or a.patient.id = :patientId) "
			+ "and (:doctorId is null or a.doctor.id = :doctorId) "
			+ "and (:status is null or :status = '' or upper(a.status) = upper(:status)) "
			+ "and (:date is null or a.appointmentDate = :date) "
			+ "order by a.appointmentDate desc, a.appointmentTime desc")
	List<Appointment> search(@Param("patientId") Long patientId, @Param("doctorId") Long doctorId,
			@Param("status") String status, @Param("date") LocalDate date);

	@Query("select count(a) > 0 from Appointment a where a.doctor.id = :doctorId and a.appointmentDate = :date "
			+ "and a.appointmentTime = :time and a.status <> 'CANCELLED' and (:excludeId is null or a.id <> :excludeId)")
	boolean slotTaken(@Param("doctorId") Long doctorId, @Param("date") LocalDate date, @Param("time") LocalTime time,
			@Param("excludeId") Long excludeId);

	@Query("select a.appointmentTime from Appointment a where a.doctor.id = :doctorId and a.appointmentDate = :date and a.status <> 'CANCELLED'")
	List<LocalTime> bookedTimes(@Param("doctorId") Long doctorId, @Param("date") LocalDate date);

	long countByAppointmentDate(LocalDate date);

	long countByStatusIgnoreCase(String status);

	long countByDoctorId(Long doctorId);

	List<Appointment> findTop5ByOrderByAppointmentDateDescAppointmentTimeDesc();

	List<Appointment> findByPatientFullNameContainingIgnoreCase(String name);
}
