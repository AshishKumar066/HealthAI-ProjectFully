import { apiRequest } from "./api";

export function getAppointments(params = {}) {
  const query = new URLSearchParams();
  if (params.patientId) query.set("patientId", params.patientId);
  if (params.doctorId) query.set("doctorId", params.doctorId);
  if (params.status) query.set("status", params.status);
  if (params.date) query.set("date", params.date);
  const suffix = query.toString() ? `?${query.toString()}` : "";
  return apiRequest(`/appointments${suffix}`);
}

export function getAppointmentSlots(doctorId, date) {
  return apiRequest(`/appointments/slots?doctorId=${doctorId}&date=${date}`);
}

export function createAppointment(payload) {
  return apiRequest("/appointments", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export function updateAppointmentStatus(id, status) {
  return apiRequest(`/appointments/${id}/status`, {
    method: "PATCH",
    body: JSON.stringify({ status }),
  });
}
