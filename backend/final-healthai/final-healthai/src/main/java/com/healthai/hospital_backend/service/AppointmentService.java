package com.healthai.hospital_backend.service;

import com.healthai.hospital_backend.dto.Requests.AppointmentReq;
import com.healthai.hospital_backend.entity.Appointment;
import com.healthai.hospital_backend.entity.Doctor;
import com.healthai.hospital_backend.exception.BadRequestException;
import com.healthai.hospital_backend.exception.ResourceNotFoundException;
import com.healthai.hospital_backend.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class AppointmentService {
	private static final List<String> STATUSES = List.of("SCHEDULED", "CONFIRMED", "COMPLETED", "CANCELLED");

	private final AppointmentRepository repo;
	private final PatientService patients;
	private final DoctorService doctors;
	private final NotificationService notifications;
	private final Clock clock;

	public AppointmentService(AppointmentRepository repo, PatientService patients, DoctorService doctors,
			NotificationService notifications, Clock clock) {
		this.repo = repo;
		this.patients = patients;
		this.doctors = doctors;
		this.notifications = notifications;
		this.clock = clock;
	}

	public List<Appointment> search(Long patientId, Long doctorId, String status, LocalDate date) {
		return repo.search(patientId, doctorId, status, date);
	}

	public Appointment get(Long id) {
		return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Appointment", id));
	}

	/** Every slot for a doctor on a date, marked available / booked. */
	public List<Map<String, Object>> slots(Long doctorId, LocalDate date) {
		Doctor d = doctors.get(doctorId);
		Set<LocalTime> booked = new HashSet<>(repo.bookedTimes(doctorId, date));
		boolean working = "AVAILABLE".equalsIgnoreCase(d.getAvailability());
		int step = d.getSlotMinutes() == null || d.getSlotMinutes() <= 0 ? 30 : d.getSlotMinutes();
		List<Map<String, Object>> out = new ArrayList<>();
		for (LocalTime t = d.getWorkStart(); t.isBefore(d.getWorkEnd()); t = t.plusMinutes(step)) {
			boolean past = LocalDateTime.of(date, t).isBefore(LocalDateTime.now(clock));
			Map<String, Object> m = new LinkedHashMap<>();
			m.put("time", t.toString());
			m.put("available", working && !past && !booked.contains(t));
			out.add(m);
			if (t.plusMinutes(step).isBefore(t))
				break; // midnight wrap guard
		}
		return out;
	}

	public Appointment create(AppointmentReq r) {
		Doctor d = doctors.get(r.doctorId());
		if ("ON_LEAVE".equalsIgnoreCase(d.getAvailability()))
			throw new BadRequestException(d.getFullName() + " is on leave");
		if (LocalDateTime.of(r.appointmentDate(), r.appointmentTime())
				.isBefore(LocalDateTime.now(clock).minusMinutes(1)))
			throw new BadRequestException("Cannot book an appointment in the past");
		if (repo.slotTaken(r.doctorId(), r.appointmentDate(), r.appointmentTime(), null))
			throw new BadRequestException("That time slot is already booked. Please pick another slot.");
		Appointment a = new Appointment();
		a.setPatient(patients.get(r.patientId()));
		a.setDoctor(d);
		a.setAppointmentDate(r.appointmentDate());
		a.setAppointmentTime(r.appointmentTime());
		a.setReason(r.reason());
		a.setNotes(r.notes());
		a.setStatus(validStatus(r.status(), "SCHEDULED"));
		Appointment saved = repo.save(a);
		notifications.push("APPOINTMENT", "New appointment booked", saved.getPatient().getFullName() + " with "
				+ d.getFullName() + " on " + saved.getAppointmentDate() + " at " + saved.getAppointmentTime());
		return saved;
	}

	public Appointment update(Long id, AppointmentReq r) {
		Appointment a = get(id);
		if (repo.slotTaken(r.doctorId(), r.appointmentDate(), r.appointmentTime(), id))
			throw new BadRequestException("That time slot is already booked. Please pick another slot.");
		a.setPatient(patients.get(r.patientId()));
		a.setDoctor(doctors.get(r.doctorId()));
		a.setAppointmentDate(r.appointmentDate());
		a.setAppointmentTime(r.appointmentTime());
		a.setReason(r.reason());
		a.setNotes(r.notes());
		if (r.status() != null)
			a.setStatus(validStatus(r.status(), a.getStatus()));
		return repo.save(a);
	}

	public Appointment updateStatus(Long id, String status) {
		Appointment a = get(id);
		a.setStatus(validStatus(status, null));
		Appointment saved = repo.save(a);
		notifications.push("APPOINTMENT", "Appointment " + saved.getStatus().toLowerCase(),
				saved.getPatient().getFullName() + " / " + saved.getDoctor().getFullName() + " ("
						+ saved.getAppointmentDate() + ")");
		return saved;
	}

	public void delete(Long id) {
		repo.delete(get(id));
	}

	private String validStatus(String s, String fallback) {
		if (s == null || s.isBlank())
			return fallback;
		String u = s.toUpperCase();
		if (!STATUSES.contains(u))
			throw new BadRequestException("Status must be one of " + STATUSES);
		return u;
	}
}
