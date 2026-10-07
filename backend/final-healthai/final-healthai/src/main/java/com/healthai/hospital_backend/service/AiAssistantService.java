package com.healthai.hospital_backend.service;

import com.healthai.hospital_backend.entity.*;
import com.healthai.hospital_backend.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

/**
 * Health assistant. Answers hospital questions from REAL database data
 * (doctors, appointments, hospitals) and gives general health information with
 * a safety disclaimer. It never diagnoses. To plug in an LLM later, replace
 * {@link #reply(String)} with a call to your AI provider.
 */
@Service
public class AiAssistantService {
	private static final String DISCLAIMER = "\n\n_This is general information, not a medical diagnosis. Please consult a doctor for medical advice._";
	private final DoctorRepository doctors;
	private final DepartmentRepository departments;
	private final AppointmentRepository appointments;
	private final HospitalRepository hospitals;

	public AiAssistantService(DoctorRepository doctors, DepartmentRepository departments,
			AppointmentRepository appointments, HospitalRepository hospitals) {
		this.doctors = doctors;
		this.departments = departments;
		this.appointments = appointments;
		this.hospitals = hospitals;
	}

	public Map<String, Object> reply(String message) {
		String m = message.toLowerCase();
		String answer;
		String action = null;

		if (has(m, "chest pain", "can't breathe", "cannot breathe", "breathing difficulty", "unconscious", "stroke",
				"heavy bleeding", "suicid", "overdose", "seizure")) {
			answer = "**This may be an emergency.** Call **108** (ambulance) or go to the nearest emergency room immediately. Do not wait for an appointment.";
			action = "EMERGENCY";
		} else if (has(m, "emergency", "ambulance", "nearest hospital", "hospital near")) {
			List<Hospital> er = hospitals.findAll().stream().filter(Hospital::isEmergencyAvailable).limit(3).toList();
			StringBuilder sb = new StringBuilder("For emergencies call **108**. Hospitals with emergency care:\n");
			er.forEach(h -> sb.append("- ").append(h.getName()).append(" (").append(h.getCity()).append(") – ")
					.append(h.getPhone()).append(", ICU beds free: ").append(h.getAvailableIcuBeds()).append("\n"));
			answer = sb.toString();
			action = "EMERGENCY";
		} else if (has(m, "available doctor", "which doctors", "doctors available", "list doctors", "show doctors")) {
			List<Doctor> list = doctors.search(null, null, "AVAILABLE");
			StringBuilder sb = new StringBuilder("Doctors available right now:\n");
			list.stream().limit(8).forEach(d -> sb.append("- ").append(d.getFullName()).append(" – ")
					.append(d.getSpecialization()).append("\n"));
			if (list.isEmpty())
				sb.append("No doctors are marked available at the moment.");
			answer = sb.toString();
		} else if (has(m, "book", "appointment")) {
			String spec = departments.findAll().stream().map(Department::getName)
					.filter(n -> m.contains(n.toLowerCase())).findFirst().orElse(null);
			List<Doctor> list = spec == null ? doctors.search(null, null, "AVAILABLE")
					: doctors.findAll().stream().filter(
							d -> d.getDepartment() != null && d.getDepartment().getName().equalsIgnoreCase(spec))
							.toList();
			StringBuilder sb = new StringBuilder("You can book from the **Appointments** page (New Appointment).");
			if (!list.isEmpty()) {
				sb.append(spec == null ? " Available doctors:\n" : " " + spec + " doctors:\n");
				list.stream().limit(5).forEach(d -> sb.append("- ").append(d.getFullName()).append(" – ")
						.append(d.getSpecialization()).append("\n"));
			}
			answer = sb.toString();
			action = "OPEN_APPOINTMENTS";
		} else if (has(m, "today", "my appointments", "next appointment", "upcoming")) {
			long n = appointments.countByAppointmentDate(LocalDate.now());
			answer = "There are **" + n
					+ "** appointment(s) scheduled for today. Open the Appointments page to see details.";
			action = "OPEN_APPOINTMENTS";
		} else if (has(m, "fever", "cold", "cough", "headache", "stomach", "pain", "vomit", "diarrh", "rash",
				"sore throat")) {
			answer = "For mild symptoms like fever, cough or headache: rest, drink plenty of fluids and monitor your temperature. See a doctor if symptoms last more than 2–3 days, get worse, or you have high fever, breathing trouble or severe pain."
					+ DISCLAIMER;
		} else if (m.matches(".*\\b(hello|hi|hey|namaste)\\b.*")) {
			answer = "Hello! I'm the HealthAI assistant. I can help you find doctors, book appointments, find emergency hospitals or share general health information.";
		} else {
			answer = "I can help with: finding available doctors, booking appointments, emergency hospitals, and general health tips. Try asking *\"Which doctors are available?\"*"
					+ DISCLAIMER;
		}
		Map<String, Object> out = new LinkedHashMap<>();
		out.put("reply", answer);
		out.put("action", action);
		return out;
	}

	private boolean has(String text, String... keys) {
		for (String k : keys)
			if (text.contains(k))
				return true;
		return false;
	}
}
