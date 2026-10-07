package com.healthai.hospital_backend.service;

import com.healthai.hospital_backend.entity.Hospital;
import com.healthai.hospital_backend.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
public class DashboardService {
	private final PatientRepository patients;
	private final DoctorRepository doctors;
	private final AppointmentRepository appointments;
	private final InvoiceRepository invoices;
	private final HospitalRepository hospitals;
	private final EmergencyCaseRepository cases;
	private final PrescriptionRepository prescriptions;
	private final DepartmentRepository departments;

	public DashboardService(PatientRepository patients, DoctorRepository doctors, AppointmentRepository appointments,
			InvoiceRepository invoices, HospitalRepository hospitals, EmergencyCaseRepository cases,
			PrescriptionRepository prescriptions, DepartmentRepository departments) {
		this.patients = patients;
		this.doctors = doctors;
		this.appointments = appointments;
		this.invoices = invoices;
		this.hospitals = hospitals;
		this.cases = cases;
		this.prescriptions = prescriptions;
		this.departments = departments;
	}

	public Map<String, Object> stats() {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put("totalPatients", patients.count());
		m.put("activePatients", patients.countByStatusIgnoreCase("Active"));
		m.put("totalDoctors", doctors.count());
		m.put("availableDoctors", doctors.countByAvailabilityIgnoreCase("AVAILABLE"));
		m.put("totalDepartments", departments.count());
		m.put("totalAppointments", appointments.count());
		m.put("appointmentsToday", appointments.countByAppointmentDate(LocalDate.now()));
		m.put("pendingAppointments", appointments.countByStatusIgnoreCase("SCHEDULED"));
		m.put("totalPrescriptions", prescriptions.count());
		m.put("totalRevenue", invoices.totalRevenue());
		m.put("outstandingAmount", invoices.totalOutstanding());
		m.put("pendingBills",
				invoices.countByStatusIgnoreCase("PENDING") + invoices.countByStatusIgnoreCase("PARTIAL"));
		m.put("activeEmergencies", cases.countByStatusNot("RESOLVED"));
		int beds = 0, free = 0, icu = 0, freeIcu = 0;
		for (Hospital h : hospitals.findAll()) {
			beds += nz(h.getTotalBeds());
			free += nz(h.getAvailableBeds());
			icu += nz(h.getIcuBeds());
			freeIcu += nz(h.getAvailableIcuBeds());
		}
		m.put("totalBeds", beds);
		m.put("availableBeds", free);
		m.put("totalIcuBeds", icu);
		m.put("availableIcuBeds", freeIcu);
		m.put("recentAppointments", appointments.findTop5ByOrderByAppointmentDateDescAppointmentTimeDesc());

		// appointments per day for the last 7 days (for charts)
		List<Map<String, Object>> trend = new ArrayList<>();
		for (int i = 6; i >= 0; i--) {
			LocalDate d = LocalDate.now().minusDays(i);
			trend.add(Map.of("date", d.toString(), "count", appointments.countByAppointmentDate(d)));
		}
		m.put("appointmentTrend", trend);
		return m;
	}

	private int nz(Integer i) {
		return i == null ? 0 : i;
	}
}
