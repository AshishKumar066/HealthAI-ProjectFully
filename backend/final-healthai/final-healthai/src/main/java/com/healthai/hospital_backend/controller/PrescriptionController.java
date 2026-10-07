package com.healthai.hospital_backend.controller;

import com.healthai.hospital_backend.dto.Requests.PrescriptionReq;
import com.healthai.hospital_backend.entity.Prescription;
import com.healthai.hospital_backend.service.PrescriptionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionController {
	private final PrescriptionService service;

	public PrescriptionController(PrescriptionService service) {
		this.service = service;
	}

	@GetMapping
	public List<Prescription> list(@RequestParam(required = false) Long patientId,
			@RequestParam(required = false) Long doctorId, @RequestParam(required = false) String q) {
		return service.search(patientId, doctorId, q);
	}

	@GetMapping("/{id}")
	public Prescription get(@PathVariable Long id) {
		return service.get(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Prescription create(@Valid @RequestBody PrescriptionReq r) {
		return service.create(r);
	}

	@PutMapping("/{id}")
	public Prescription update(@PathVariable Long id, @Valid @RequestBody PrescriptionReq r) {
		return service.update(id, r);
	}

	@DeleteMapping("/{id}")
	public Map<String, String> delete(@PathVariable Long id) {
		service.delete(id);
		return Map.of("message", "Prescription deleted successfully");
	}
}
