package com.healthai.hospital_backend.controller;

import com.healthai.hospital_backend.dto.Requests.*;
import com.healthai.hospital_backend.entity.Appointment;
import com.healthai.hospital_backend.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
	private final AppointmentService service;

	public AppointmentController(AppointmentService service) {
		this.service = service;
	}

	@GetMapping
	public List<Appointment> list(@RequestParam(required = false) Long patientId,
			@RequestParam(required = false) Long doctorId, @RequestParam(required = false) String status,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		return service.search(patientId, doctorId, status, date);
	}

	/**
	 * GET /api/appointments/slots?doctorId=1&date=2026-10-05 -> [{time:"09:00",
	 * available:true}, ...]
	 */
	@GetMapping("/slots")
	public List<Map<String, Object>> slots(@RequestParam Long doctorId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		return service.slots(doctorId, date);
	}

	@GetMapping("/{id}")
	public Appointment get(@PathVariable Long id) {
		return service.get(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Appointment create(@Valid @RequestBody AppointmentReq r) {
		return service.create(r);
	}

	@PutMapping("/{id}")
	public Appointment update(@PathVariable Long id, @Valid @RequestBody AppointmentReq r) {
		return service.update(id, r);
	}

	/** Confirm / Complete / Cancel */
	@PatchMapping("/{id}/status")
	public Appointment status(@PathVariable Long id, @Valid @RequestBody StatusReq r) {
		return service.updateStatus(id, r.status());
	}

	@DeleteMapping("/{id}")
	public Map<String, String> delete(@PathVariable Long id) {
		service.delete(id);
		return Map.of("message", "Appointment deleted successfully");
	}
}
