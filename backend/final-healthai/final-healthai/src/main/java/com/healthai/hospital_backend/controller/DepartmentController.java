package com.healthai.hospital_backend.controller;

import com.healthai.hospital_backend.entity.Department;
import com.healthai.hospital_backend.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
	private final DepartmentService service;

	public DepartmentController(DepartmentService service) {
		this.service = service;
	}

	@GetMapping
	public List<Map<String, Object>> list() {
		return service.listWithCounts();
	}

	@GetMapping("/{id}")
	public Department get(@PathVariable Long id) {
		return service.get(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Department create(@Valid @RequestBody Department d) {
		return service.create(d);
	}

	@PutMapping("/{id}")
	public Department update(@PathVariable Long id, @Valid @RequestBody Department d) {
		return service.update(id, d);
	}

	@DeleteMapping("/{id}")
	public Map<String, String> delete(@PathVariable Long id) {
		service.delete(id);
		return Map.of("message", "Department deleted successfully");
	}
}
