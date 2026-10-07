package com.healthai.hospital_backend.controller;

import com.healthai.hospital_backend.dto.Requests.*;
import com.healthai.hospital_backend.entity.Invoice;
import com.healthai.hospital_backend.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
	private final InvoiceService service;

	public InvoiceController(InvoiceService service) {
		this.service = service;
	}

	@GetMapping
	public List<Invoice> list(@RequestParam(required = false) Long patientId,
			@RequestParam(required = false) String status, @RequestParam(required = false) String q) {
		return service.search(patientId, status, q);
	}

	@GetMapping("/{id}")
	public Invoice get(@PathVariable Long id) {
		return service.get(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Invoice create(@Valid @RequestBody InvoiceReq r) {
		return service.create(r);
	}

	@PutMapping("/{id}")
	public Invoice update(@PathVariable Long id, @Valid @RequestBody InvoiceReq r) {
		return service.update(id, r);
	}

	/** Record a (partial) payment */
	@PostMapping("/{id}/payments")
	public Invoice pay(@PathVariable Long id, @Valid @RequestBody PaymentReq r) {
		return service.pay(id, r);
	}

	@DeleteMapping("/{id}")
	public Map<String, String> delete(@PathVariable Long id) {
		service.delete(id);
		return Map.of("message", "Invoice deleted successfully");
	}
}
