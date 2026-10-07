package com.healthai.hospital_backend.config;

import com.healthai.hospital_backend.entity.*;
import com.healthai.hospital_backend.repository.*;
import com.healthai.hospital_backend.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/** Inserts demo data the first time the app starts on an empty database. */
@Component
public class DataSeeder implements CommandLineRunner {
	private final UserRepository users;
	private final AuthService auth;
	private final DepartmentRepository departments;
	private final DoctorRepository doctors;
	private final PatientRepository patients;
	private final HospitalRepository hospitals;
	private final NotificationRepository notifications;
	private final boolean seed;

	public DataSeeder(UserRepository users, AuthService auth, DepartmentRepository departments,
			DoctorRepository doctors, PatientRepository patients, HospitalRepository hospitals,
			NotificationRepository notifications, AppProperties props) {
		this.users = users;
		this.auth = auth;
		this.departments = departments;
		this.doctors = doctors;
		this.patients = patients;
		this.hospitals = hospitals;
		this.notifications = notifications;
		this.seed = props.seedDemoData();
	}

	@Override
	public void run(String... args) {
		if (!seed)
			return;
		if (users.count() == 0) {
			auth.create("Admin User", "admin@healthai.com", "admin123", Role.ADMIN);
			auth.create("Reception Desk", "reception@healthai.com", "reception123", Role.RECEPTIONIST);
			auth.create("Dr. Ankit Sharma", "doctor@healthai.com", "doctor123", Role.DOCTOR);
			System.out.println(
					">>> Demo logins: admin@healthai.com/admin123, reception@healthai.com/reception123, doctor@healthai.com/doctor123");
		}
		if (departments.count() == 0) {
			Department card = dep("Cardiology", "Heart and blood vessel care", "Dr. Ankit Sharma", "Block A, Floor 2");
			Department neuro = dep("Neurology", "Brain and nervous system", "Dr. Meera Iyer", "Block B, Floor 1");
			Department ortho = dep("Orthopedics", "Bones, joints and muscles", "Dr. Rohan Verma", "Block A, Floor 1");
			Department peds = dep("Pediatrics", "Child healthcare", "Dr. Sneha Kapoor", "Block C, Floor 1");
			Department gen = dep("General Medicine", "Primary and general care", "Dr. Arjun Singh", "Block D, Ground");
			doc("Dr. Ankit Sharma", "Cardiologist", card, 12, "MD, DM Cardiology", "9810000001", "ankit@healthai.com",
					"AVAILABLE", 800);
			doc("Dr. Meera Iyer", "Neurologist", neuro, 9, "MD, DM Neurology", "9810000002", "meera@healthai.com",
					"AVAILABLE", 900);
			doc("Dr. Rohan Verma", "Orthopedic Surgeon", ortho, 15, "MS Orthopedics", "9810000003",
					"rohan@healthai.com", "BUSY", 700);
			doc("Dr. Sneha Kapoor", "Pediatrician", peds, 7, "MD Pediatrics", "9810000004", "sneha@healthai.com",
					"AVAILABLE", 500);
			doc("Dr. Arjun Singh", "General Physician", gen, 5, "MBBS, MD", "9810000005", "arjun@healthai.com",
					"AVAILABLE", 400);
		}
		if (patients.count() == 0) {
			pat("Rahul Sharma", 28, "Male", "9876543210", "rahul@example.com", "O+", "Noida, Uttar Pradesh", "Fever");
			pat("Priya Patel", 34, "Female", "9876543211", "priya@example.com", "A+", "Delhi", "Hypertension");
			pat("Amit Kumar", 52, "Male", "9876543212", "amit@example.com", "B+", "Gurugram, Haryana", "Diabetes");
		}
		if (hospitals.count() == 0) {
			hos("HealthAI City Hospital", "Sector 62, Noida", "Noida", "0120-4000100", 28.6208, 77.3639, 200, 38, 30, 6,
					"Emergency, ICU, Trauma, Cardiology, Neurology");
			hos("AIIMS Delhi", "Ansari Nagar, New Delhi", "Delhi", "011-26588500", 28.5672, 77.2100, 2000, 120, 150, 14,
					"Emergency, ICU, Trauma, Burn Unit, Cardiology");
			hos("Max Super Speciality Hospital", "Saket, New Delhi", "Delhi", "011-26515050", 28.5275, 77.2127, 500, 55,
					60, 9, "Emergency, ICU, Oncology, Orthopedics");
			hos("Fortis Hospital Noida", "Sector 62, Noida", "Noida", "0120-4300222", 28.6129, 77.3687, 300, 42, 40, 7,
					"Emergency, ICU, Cardiology");
			hos("Medanta The Medicity", "Sector 38, Gurugram", "Gurugram", "0124-4141414", 28.4395, 77.0426, 1250, 90,
					120, 18, "Emergency, ICU, Trauma, Transplant");
		}
		if (notifications.count() == 0) {
			Notification n = new Notification();
			n.setType("SYSTEM");
			n.setTitle("Welcome to HealthAI");
			n.setMessage("Your hospital system is ready. Demo data has been loaded.");
			notifications.save(n);
		}
	}

	private Department dep(String n, String d, String head, String loc) {
		Department x = new Department();
		x.setName(n);
		x.setDescription(d);
		x.setHeadOfDepartment(head);
		x.setLocation(loc);
		return departments.save(x);
	}

	private void doc(String name, String spec, Department dep, int exp, String qual, String phone, String email,
			String avail, int fee) {
		Doctor d = new Doctor();
		d.setFullName(name);
		d.setSpecialization(spec);
		d.setDepartment(dep);
		d.setExperienceYears(exp);
		d.setQualification(qual);
		d.setPhone(phone);
		d.setEmail(email);
		d.setAvailability(avail);
		d.setConsultationFee(BigDecimal.valueOf(fee));
		d.setWorkStart(LocalTime.of(9, 0));
		d.setWorkEnd(LocalTime.of(17, 0));
		d.setSlotMinutes(30);
		doctors.save(d);
	}

	private void pat(String name, int age, String gender, String phone, String email, String bg, String addr,
			String cond) {
		Patient p = new Patient();
		p.setFullName(name);
		p.setAge(age);
		p.setGender(gender);
		p.setPhone(phone);
		p.setEmail(email);
		p.setBloodGroup(bg);
		p.setAddress(addr);
		p.setMedicalCondition(cond);
		p.setEmergencyContact("9876500000");
		p.setStatus("Active");
		patients.save(p);
	}

	private void hos(String name, String addr, String city, String phone, double lat, double lng, int beds, int free,
			int icu, int freeIcu, String services) {
		Hospital h = new Hospital();
		h.setName(name);
		h.setAddress(addr);
		h.setCity(city);
		h.setPhone(phone);
		h.setLatitude(lat);
		h.setLongitude(lng);
		h.setEmergencyAvailable(true);
		h.setTotalBeds(beds);
		h.setAvailableBeds(free);
		h.setIcuBeds(icu);
		h.setAvailableIcuBeds(freeIcu);
		h.setServices(services);
		hospitals.save(h);
	}
}
