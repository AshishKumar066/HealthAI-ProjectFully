package com.healthai.hospital_backend.service;

import com.healthai.hospital_backend.dto.Requests.PrescriptionReq;
import com.healthai.hospital_backend.entity.Prescription;
import com.healthai.hospital_backend.exception.ResourceNotFoundException;
import com.healthai.hospital_backend.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PrescriptionService {
	private final PrescriptionRepository repo;
	private final PatientService patients;
	private final DoctorService doctors;

	public PrescriptionService(PrescriptionRepository repo, PatientService patients, DoctorService doctors) {
		this.repo = repo;
		this.patients = patients;
		this.doctors = doctors;
	}

	public List<Prescription> search(Long patientId, Long doctorId, String q) {
		return repo.search(patientId, doctorId, q);
	}

	public Prescription get(Long id) {
		return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Prescription", id));
	}

	public Prescription create(PrescriptionReq r) {
		return repo.save(apply(new Prescription(), r));
	}

	public Prescription update(Long id, PrescriptionReq r) {
		return repo.save(apply(get(id), r));
	}

	public void delete(Long id) {
		repo.delete(get(id));
	}

	private Prescription apply(Prescription p, PrescriptionReq r) {
		p.setPatient(patients.get(r.patientId()));
		p.setDoctor(doctors.get(r.doctorId()));
		p.setMedicine(r.medicine());
		p.setDosage(r.dosage());
		p.setFrequency(r.frequency());
		p.setDurationDays(r.durationDays());
		p.setInstructions(r.instructions());
		if (r.status() != null)
			p.setStatus(r.status().toUpperCase());
		return p;
	}
}
