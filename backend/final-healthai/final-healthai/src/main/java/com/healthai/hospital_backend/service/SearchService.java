package com.healthai.hospital_backend.service;

import com.healthai.hospital_backend.entity.*;
import com.healthai.hospital_backend.repository.*;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Powers the navbar global search. Each result: {type, id, title, subtitle,
 * route}.
 */
@Service
public class SearchService {
	private final PatientRepository patients;
	private final DoctorRepository doctors;
	private final AppointmentRepository appointments;
	private final PrescriptionRepository prescriptions;

	public SearchService(PatientRepository patients, DoctorRepository doctors, AppointmentRepository appointments,
			PrescriptionRepository prescriptions) {
		this.patients = patients;
		this.doctors = doctors;
		this.appointments = appointments;
		this.prescriptions = prescriptions;
	}

	public List<Map<String, Object>> search(String q) {
		List<Map<String, Object>> out = new ArrayList<>();
		if (q == null || q.trim().length() < 2)
			return out;
		for (Patient p : patients.search(q, null))
			out.add(item("PATIENT", p.getId(), p.getFullName(),
					p.getPatientCode() + (p.getMedicalCondition() == null ? "" : " · " + p.getMedicalCondition()),
					"/patients"));
		for (Doctor d : doctors.search(q, null, null))
			out.add(item("DOCTOR", d.getId(), d.getFullName(), d.getSpecialization(), "/doctors"));
		for (Appointment a : appointments.findByPatientFullNameContainingIgnoreCase(q))
			out.add(item("APPOINTMENT", a.getId(), a.getPatient().getFullName(),
					a.getAppointmentDate() + " · " + a.getAppointmentTime() + " · " + a.getDoctor().getFullName(),
					"/appointments"));
		for (Prescription p : prescriptions.findByMedicineContainingIgnoreCaseOrPatientFullNameContainingIgnoreCase(q,
				q))
			out.add(item("PRESCRIPTION", p.getId(), p.getPatient().getFullName(), p.getMedicine(), "/prescriptions"));
		return out.size() > 25 ? out.subList(0, 25) : out;
	}

	private Map<String, Object> item(String type, Long id, String title, String sub, String route) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("type", type);
		m.put("id", id);
		m.put("title", title);
		m.put("subtitle", sub);
		m.put("route", route);
		return m;
	}
}
