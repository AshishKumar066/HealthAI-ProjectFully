import { apiRequest } from "./api";

/** Public hospital records from the Spring Boot backend. */
export function getHospitals(params = {}) {
  const query = new URLSearchParams();
  if (params.q) query.set("q", params.q);
  const suffix = query.toString() ? `?${query.toString()}` : "";
  return apiRequest(`/hospitals${suffix}`);
}

export function getHospital(id) {
  return apiRequest(`/hospitals/${id}`);
}
