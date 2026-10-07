package com.healthai.hospital_backend.service;

import com.healthai.hospital_backend.dto.Requests.RecordReq;
import com.healthai.hospital_backend.entity.MedicalRecord;
import com.healthai.hospital_backend.exception.BadRequestException;
import com.healthai.hospital_backend.exception.ResourceNotFoundException;
import com.healthai.hospital_backend.repository.MedicalRecordRepository;
import com.healthai.hospital_backend.config.AppProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class MedicalRecordService {
	private final MedicalRecordRepository repo;
	private final PatientService patients;
	private final DoctorService doctors;
	private final Path dir;

	public MedicalRecordService(MedicalRecordRepository repo, PatientService patients, DoctorService doctors,
			AppProperties props) {
		this.repo = repo;
		this.patients = patients;
		this.doctors = doctors;
		this.dir = Paths.get(props.uploadDir()).toAbsolutePath().normalize();
	}

	/** Lifecycle callback: runs once after the bean is created and injected. */
	@PostConstruct
	void init() throws IOException {
		Files.createDirectories(dir);
	}

	public List<MedicalRecord> search(Long patientId, String type, String q) {
		return repo.search(patientId, type, q);
	}

	public MedicalRecord get(Long id) {
		return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Medical record", id));
	}

	public MedicalRecord create(RecordReq r, MultipartFile file) {
		MedicalRecord m = new MedicalRecord();
		apply(m, r);
		if (file != null && !file.isEmpty())
			store(m, file);
		return repo.save(m);
	}

	public MedicalRecord update(Long id, RecordReq r, MultipartFile file) {
		MedicalRecord m = get(id);
		apply(m, r);
		if (file != null && !file.isEmpty()) {
			deleteFile(m);
			store(m, file);
		}
		return repo.save(m);
	}

	public Resource file(MedicalRecord m) {
		if (m.getStoredFileName() == null)
			throw new ResourceNotFoundException("File for medical record", m.getId());
		try {
			Resource res = new UrlResource(dir.resolve(m.getStoredFileName()).normalize().toUri());
			if (!res.exists())
				throw new ResourceNotFoundException("File for medical record", m.getId());
			return res;
		} catch (java.net.MalformedURLException e) {
			throw new BadRequestException("Bad file path");
		}
	}

	public void delete(Long id) {
		MedicalRecord m = get(id);
		deleteFile(m);
		repo.delete(m);
	}

	private void apply(MedicalRecord m, RecordReq r) {
		m.setPatient(patients.get(r.patientId()));
		m.setDoctor(r.doctorId() == null ? null : doctors.get(r.doctorId()));
		m.setRecordType(r.recordType());
		m.setTitle(r.title());
		m.setDescription(r.description());
		m.setRecordDate(r.recordDate());
	}

	private void store(MedicalRecord m, MultipartFile f) {
		String original = Paths.get(f.getOriginalFilename() == null ? "file" : f.getOriginalFilename()).getFileName()
				.toString();
		String stored = UUID.randomUUID() + "-" + original.replaceAll("[^A-Za-z0-9._-]", "_");
		try {
			Files.copy(f.getInputStream(), dir.resolve(stored), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new BadRequestException("Could not save file: " + e.getMessage());
		}
		m.setFileName(original);
		m.setStoredFileName(stored);
		m.setContentType(f.getContentType());
		m.setFileSize(f.getSize());
	}

	private void deleteFile(MedicalRecord m) {
		if (m.getStoredFileName() == null)
			return;
		try {
			Files.deleteIfExists(dir.resolve(m.getStoredFileName()));
		} catch (IOException ignored) {
		}
		m.setStoredFileName(null);
		m.setFileName(null);
		m.setContentType(null);
		m.setFileSize(null);
	}
}
