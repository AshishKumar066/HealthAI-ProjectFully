package com.healthai.hospital_backend.controller;

import com.healthai.hospital_backend.entity.Patient;
import com.healthai.hospital_backend.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
	private final PatientService service;

	public PatientController(PatientService service) {
		this.service = service;
	}

	@GetMapping
	public List<Patient> list(@RequestParam(required = false) String q, @RequestParam(required = false) String status) {
		return service.search(q, status);
	}

	@GetMapping("/{id}")
	public Patient get(@PathVariable Long id) {
		return service.get(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Patient create(@Valid @RequestBody Patient p) {
		return service.create(p);
	}

	@PutMapping("/{id}")
	public Patient update(@PathVariable Long id, @Valid @RequestBody Patient p) {
		return service.update(id, p);
	}

	@DeleteMapping("/{id}")
	public Map<String, String> delete(@PathVariable Long id) {
		service.delete(id);
		return Map.of("message", "Patient deleted successfully");
	}
}
