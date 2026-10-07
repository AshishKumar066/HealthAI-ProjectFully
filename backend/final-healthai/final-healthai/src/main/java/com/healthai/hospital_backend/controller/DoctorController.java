package com.healthai.hospital_backend.controller;

import com.healthai.hospital_backend.dto.Requests.*;
import com.healthai.hospital_backend.entity.Doctor;
import com.healthai.hospital_backend.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
	private final DoctorService service;

	public DoctorController(DoctorService service) {
		this.service = service;
	}

	@GetMapping
	public List<Doctor> list(@RequestParam(required = false) String q,
			@RequestParam(required = false) Long departmentId, @RequestParam(required = false) String availability) {
		return service.search(q, departmentId, availability);
	}

	@GetMapping("/{id}")
	public Doctor get(@PathVariable Long id) {
		return service.get(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Doctor create(@Valid @RequestBody DoctorReq r) {
		return service.create(r);
	}

	@PutMapping("/{id}")
	public Doctor update(@PathVariable Long id, @Valid @RequestBody DoctorReq r) {
		return service.update(id, r);
	}

	/** Doctors/receptionists can toggle availability too. */
	@PatchMapping("/{id}/availability")
	public Doctor availability(@PathVariable Long id, @Valid @RequestBody StatusReq r) {
		return service.setAvailability(id, r.status());
	}

	@DeleteMapping("/{id}")
	public Map<String, String> delete(@PathVariable Long id) {
		service.delete(id);
		return Map.of("message", "Doctor deleted successfully");
	}
}
