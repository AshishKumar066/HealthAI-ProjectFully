package com.healthai.hospital_backend.service;

import com.healthai.hospital_backend.dto.Requests.DoctorReq;
import com.healthai.hospital_backend.entity.Doctor;
import com.healthai.hospital_backend.exception.BadRequestException;
import com.healthai.hospital_backend.exception.ResourceNotFoundException;
import com.healthai.hospital_backend.repository.AppointmentRepository;
import com.healthai.hospital_backend.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DoctorService {
	private final DoctorRepository repo;
	private final DepartmentService departments;
	private final AppointmentRepository appointments;

	public DoctorService(DoctorRepository repo, DepartmentService departments, AppointmentRepository appointments) {
		this.repo = repo;
		this.departments = departments;
		this.appointments = appointments;
	}

	public List<Doctor> search(String q, Long departmentId, String availability) {
		return repo.search(q, departmentId, availability);
	}

	public Doctor get(Long id) {
		return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Doctor", id));
	}

	public Doctor create(DoctorReq r) {
		return repo.save(apply(new Doctor(), r));
	}

	public Doctor update(Long id, DoctorReq r) {
		return repo.save(apply(get(id), r));
	}

	public Doctor setAvailability(Long id, String availability) {
		String a = availability.toUpperCase();
		if (!List.of("AVAILABLE", "BUSY", "ON_LEAVE").contains(a))
			throw new BadRequestException("Availability must be AVAILABLE, BUSY or ON_LEAVE");
		Doctor d = get(id);
		d.setAvailability(a);
		return repo.save(d);
	}

	public void delete(Long id) {
		if (appointments.countByDoctorId(id) > 0)
			throw new BadRequestException("This doctor has appointments. Set status to ON_LEAVE instead of deleting.");
		repo.delete(get(id));
	}

	private Doctor apply(Doctor d, DoctorReq r) {
		d.setFullName(r.fullName());
		d.setSpecialization(r.specialization());
		d.setDepartment(r.departmentId() == null ? null : departments.get(r.departmentId()));
		d.setExperienceYears(r.experienceYears());
		d.setQualification(r.qualification());
		d.setPhone(r.phone());
		d.setEmail(r.email());
		if (r.availability() != null)
			d.setAvailability(r.availability().toUpperCase());
		d.setConsultationFee(r.consultationFee());
		if (r.workStart() != null)
			d.setWorkStart(r.workStart());
		if (r.workEnd() != null)
			d.setWorkEnd(r.workEnd());
		if (r.slotMinutes() != null && r.slotMinutes() > 0)
			d.setSlotMinutes(r.slotMinutes());
		return d;
	}
}
