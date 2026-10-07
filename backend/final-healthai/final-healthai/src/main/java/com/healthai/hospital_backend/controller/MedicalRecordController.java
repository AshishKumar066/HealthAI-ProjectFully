package com.healthai.hospital_backend.controller;

import com.healthai.hospital_backend.dto.Requests.RecordReq;
import com.healthai.hospital_backend.entity.MedicalRecord;
import com.healthai.hospital_backend.service.MedicalRecordService;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Create/update use multipart/form-data so a file can be attached (file is
 * optional).
 */
@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {
	private final MedicalRecordService service;

	public MedicalRecordController(MedicalRecordService service) {
		this.service = service;
	}

	@GetMapping
	public List<MedicalRecord> list(@RequestParam(required = false) Long patientId,
			@RequestParam(required = false) String type, @RequestParam(required = false) String q) {
		return service.search(patientId, type, q);
	}

	@GetMapping("/{id}")
	public MedicalRecord get(@PathVariable Long id) {
		return service.get(id);
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public MedicalRecord create(@RequestParam Long patientId, @RequestParam(required = false) Long doctorId,
			@RequestParam String recordType, @RequestParam String title,
			@RequestParam(required = false) String description,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordDate,
			@RequestParam(required = false) MultipartFile file) {
		return service.create(new RecordReq(patientId, doctorId, recordType, title, description, recordDate), file);
	}

	@PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public MedicalRecord update(@PathVariable Long id, @RequestParam Long patientId,
			@RequestParam(required = false) Long doctorId, @RequestParam String recordType, @RequestParam String title,
			@RequestParam(required = false) String description,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordDate,
			@RequestParam(required = false) MultipartFile file) {
		return service.update(id, new RecordReq(patientId, doctorId, recordType, title, description, recordDate), file);
	}

	/** Download (or view inline with ?inline=true) */
	@GetMapping("/{id}/file")
	public ResponseEntity<Resource> file(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean inline) {
		MedicalRecord m = service.get(id);
		Resource res = service.file(m);
		MediaType type = m.getContentType() == null ? MediaType.APPLICATION_OCTET_STREAM
				: MediaType.parseMediaType(m.getContentType());
		return ResponseEntity.ok().contentType(type).header(HttpHeaders.CONTENT_DISPOSITION,
				(inline ? "inline" : "attachment") + "; filename=\"" + m.getFileName() + "\"").body(res);
	}

	@DeleteMapping("/{id}")
	public Map<String, String> delete(@PathVariable Long id) {
		service.delete(id);
		return Map.of("message", "Medical record deleted successfully");
	}
}
