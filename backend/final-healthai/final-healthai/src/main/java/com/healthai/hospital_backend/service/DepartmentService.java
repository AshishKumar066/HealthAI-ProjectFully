package com.healthai.hospital_backend.service;

import com.healthai.hospital_backend.entity.Department;
import com.healthai.hospital_backend.exception.BadRequestException;
import com.healthai.hospital_backend.exception.ResourceNotFoundException;
import com.healthai.hospital_backend.repository.DepartmentRepository;
import com.healthai.hospital_backend.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DepartmentService {
	private final DepartmentRepository repo;
	private final DoctorRepository doctors;

	public DepartmentService(DepartmentRepository repo, DoctorRepository doctors) {
		this.repo = repo;
		this.doctors = doctors;
	}

	/**
	 * Each department plus the number of doctors in it (the Departments page shows
	 * this).
	 */
	public List<Map<String, Object>> listWithCounts() {
		List<Map<String, Object>> out = new ArrayList<>();
		for (Department d : repo.findAll()) {
			Map<String, Object> m = new LinkedHashMap<>();
			m.put("id", d.getId());
			m.put("name", d.getName());
			m.put("description", d.getDescription());
			m.put("headOfDepartment", d.getHeadOfDepartment());
			m.put("location", d.getLocation());
			m.put("doctorCount", doctors.countByDepartmentId(d.getId()));
			out.add(m);
		}
		return out;
	}

	public Department get(Long id) {
		return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department", id));
	}

	public Department create(Department d) {
		if (repo.existsByNameIgnoreCase(d.getName()))
			throw new BadRequestException("Department already exists: " + d.getName());
		d.setId(null);
		return repo.save(d);
	}

	public Department update(Long id, Department in) {
		Department d = get(id);
		d.setName(in.getName());
		d.setDescription(in.getDescription());
		d.setHeadOfDepartment(in.getHeadOfDepartment());
		d.setLocation(in.getLocation());
		return repo.save(d);
	}

	public void delete(Long id) {
		if (doctors.countByDepartmentId(id) > 0)
			throw new BadRequestException("Cannot delete a department that still has doctors");
		repo.delete(get(id));
	}
}
