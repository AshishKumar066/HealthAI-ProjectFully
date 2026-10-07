import { apiRequest } from "./api";

export function getDoctors(params = {}) {
  const query = new URLSearchParams();
  if (params.q) query.set("q", params.q);
  if (params.departmentId) query.set("departmentId", params.departmentId);
  if (params.availability) query.set("availability", params.availability);
  const suffix = query.toString() ? `?${query.toString()}` : "";
  return apiRequest(`/doctors${suffix}`);
}

export function getDoctor(id) {
  return apiRequest(`/doctors/${id}`);
}
