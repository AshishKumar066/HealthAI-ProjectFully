package com.healthai.hospital_backend.service;

import com.healthai.hospital_backend.entity.Patient;
import com.healthai.hospital_backend.exception.ResourceNotFoundException;
import com.healthai.hospital_backend.repository.PatientRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PatientService {
	private final PatientRepository repo;

	public PatientService(PatientRepository repo) {
		this.repo = repo;
	}

	public List<Patient> search(String q, String status) {
		return repo.search(q, status);
	}

	public Patient get(Long id) {
		return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Patient", id));
	}

	public Patient create(Patient p) {
		p.setId(null);
		return repo.save(p);
	}

	public Patient update(Long id, Patient in) {
		Patient p = get(id);
		p.setFullName(in.getFullName());
		p.setDateOfBirth(in.getDateOfBirth());
		p.setAge(in.getAge());
		p.setGender(in.getGender());
		p.setPhone(in.getPhone());
		p.setEmail(in.getEmail());
		p.setBloodGroup(in.getBloodGroup());
		p.setAddress(in.getAddress());
		p.setMedicalCondition(in.getMedicalCondition());
		p.setEmergencyContact(in.getEmergencyContact());
		if (in.getStatus() != null)
			p.setStatus(in.getStatus());
		return repo.save(p);
	}

	public void delete(Long id) {
		repo.delete(get(id));
	}
}
