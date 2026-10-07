package com.healthai.hospital_backend.service;

import com.healthai.hospital_backend.dto.Requests.EmergencyReq;
import com.healthai.hospital_backend.entity.EmergencyCase;
import com.healthai.hospital_backend.entity.Hospital;
import com.healthai.hospital_backend.exception.BadRequestException;
import com.healthai.hospital_backend.exception.ResourceNotFoundException;
import com.healthai.hospital_backend.repository.EmergencyCaseRepository;
import com.healthai.hospital_backend.repository.HospitalRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class HospitalService {
	private final HospitalRepository repo;
	private final EmergencyCaseRepository cases;
	private final NotificationService notifications;

	public HospitalService(HospitalRepository repo, EmergencyCaseRepository cases, NotificationService notifications) {
		this.repo = repo;
		this.cases = cases;
		this.notifications = notifications;
	}

	public List<Hospital> search(String q) {
		return (q == null || q.isBlank()) ? repo.findAll()
				: repo.findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(q, q);
	}

	public Hospital get(Long id) {
		return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hospital", id));
	}

	public Hospital create(Hospital h) {
		h.setId(null);
		return repo.save(h);
	}

	public Hospital update(Long id, Hospital in) {
		Hospital h = get(id);
		h.setName(in.getName());
		h.setAddress(in.getAddress());
		h.setCity(in.getCity());
		h.setPhone(in.getPhone());
		h.setLatitude(in.getLatitude());
		h.setLongitude(in.getLongitude());
		h.setEmergencyAvailable(in.isEmergencyAvailable());
		h.setTotalBeds(in.getTotalBeds());
		h.setAvailableBeds(in.getAvailableBeds());
		h.setIcuBeds(in.getIcuBeds());
		h.setAvailableIcuBeds(in.getAvailableIcuBeds());
		h.setServices(in.getServices());
		return repo.save(h);
	}

	public void delete(Long id) {
		repo.delete(get(id));
	}

	/**
	 * Hospitals with emergency service, sorted by distance from (lat, lng).
	 * Distance in km.
	 */
	public List<Map<String, Object>> nearest(double lat, double lng, int limit) {
		List<Map<String, Object>> out = new ArrayList<>();
		for (Hospital h : repo.findAll()) {
			if (!h.isEmergencyAvailable() || h.getLatitude() == null || h.getLongitude() == null)
				continue;
			double km = haversine(lat, lng, h.getLatitude(), h.getLongitude());
			Map<String, Object> m = new LinkedHashMap<>();
			m.put("hospital", h);
			m.put("distanceKm", Math.round(km * 10.0) / 10.0);
			m.put("directionsUrl", "https://www.google.com/maps/dir/?api=1&origin=" + lat + "," + lng + "&destination="
					+ h.getLatitude() + "," + h.getLongitude());
			out.add(m);
		}
		out.sort(Comparator.comparingDouble(m -> (Double) m.get("distanceKm")));
		return out.size() > limit ? out.subList(0, limit) : out;
	}

	static double haversine(double lat1, double lon1, double lat2, double lon2) {
		double r = 6371.0, dLat = Math.toRadians(lat2 - lat1), dLon = Math.toRadians(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
		return 2 * r * Math.asin(Math.sqrt(a));
	}

	// ---- emergency cases ----
	public List<EmergencyCase> listCases() {
		return cases.findAllByOrderByIdDesc();
	}

	public EmergencyCase reportCase(EmergencyReq r) {
		EmergencyCase c = new EmergencyCase();
		c.setPatientName(r.patientName());
		c.setPhone(r.phone());
		c.setDescription(r.description());
		c.setLocation(r.location());
		if (r.severity() != null)
			c.setSeverity(r.severity().toUpperCase());
		if (r.hospitalId() != null)
			c.setHospital(get(r.hospitalId()));
		EmergencyCase saved = cases.save(c);
		notifications.push("EMERGENCY", "Emergency case reported (" + saved.getSeverity() + ")",
				saved.getPatientName() + " - " + (saved.getDescription() == null ? "" : saved.getDescription()));
		return saved;
	}

	public EmergencyCase updateCaseStatus(Long id, String status) {
		String s = status.toUpperCase();
		if (!List.of("OPEN", "DISPATCHED", "ADMITTED", "RESOLVED").contains(s))
			throw new BadRequestException("Status must be OPEN, DISPATCHED, ADMITTED or RESOLVED");
		EmergencyCase c = cases.findById(id).orElseThrow(() -> new ResourceNotFoundException("Emergency case", id));
		c.setStatus(s);
		return cases.save(c);
	}
}
