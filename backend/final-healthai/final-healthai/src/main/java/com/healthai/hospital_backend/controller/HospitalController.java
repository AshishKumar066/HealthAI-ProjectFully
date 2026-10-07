package com.healthai.hospital_backend.controller;

import com.healthai.hospital_backend.dto.Requests.*;
import com.healthai.hospital_backend.entity.EmergencyCase;
import com.healthai.hospital_backend.entity.Hospital;
import com.healthai.hospital_backend.service.HospitalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HospitalController {
	private final HospitalService service;

	public HospitalController(HospitalService service) {
		this.service = service;
	}

	@GetMapping("/hospitals")
	public List<Hospital> list(@RequestParam(required = false) String q) {
		return service.search(q);
	}

	/**
	 * GET /api/hospitals/nearest?lat=28.61&lng=77.20 (browser geolocation ->
	 * nearest emergency hospitals)
	 */
	@GetMapping("/hospitals/nearest")
	public List<Map<String, Object>> nearest(@RequestParam double lat, @RequestParam double lng,
			@RequestParam(defaultValue = "5") int limit) {
		return service.nearest(lat, lng, limit);
	}

	@GetMapping("/hospitals/{id}")
	public Hospital get(@PathVariable Long id) {
		return service.get(id);
	}

	@PostMapping("/hospitals")
	@ResponseStatus(HttpStatus.CREATED)
	public Hospital create(@Valid @RequestBody Hospital h) {
		return service.create(h);
	}

	@PutMapping("/hospitals/{id}")
	public Hospital update(@PathVariable Long id, @Valid @RequestBody Hospital h) {
		return service.update(id, h);
	}

	@DeleteMapping("/hospitals/{id}")
	public Map<String, String> delete(@PathVariable Long id) {
		service.delete(id);
		return Map.of("message", "Hospital deleted successfully");
	}

	// ---- emergency cases ----
	@GetMapping("/emergency")
	public List<EmergencyCase> cases() {
		return service.listCases();
	}

	@PostMapping("/emergency")
	@ResponseStatus(HttpStatus.CREATED)
	public EmergencyCase report(@Valid @RequestBody EmergencyReq r) {
		return service.reportCase(r);
	}

	@PatchMapping("/emergency/{id}/status")
	public EmergencyCase caseStatus(@PathVariable Long id, @Valid @RequestBody StatusReq r) {
		return service.updateCaseStatus(id, r.status());
	}
}
