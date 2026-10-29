import { apiRequest } from "./api";

export async function login(email, password) {
  const data = await apiRequest("/auth/login", {
    method: "POST",
    body: JSON.stringify({ email: email.trim(), password }),
  });

  if (data?.token) localStorage.setItem("healthai_token", data.token);
  if (data?.user) localStorage.setItem("healthai_user", JSON.stringify(data.user));
  return data;
}

export async function signup(fullName, email, password, confirmPassword) {
  return apiRequest("/auth/register", {
    method: "POST",
    body: JSON.stringify({
      fullName: fullName.trim(),
      email: email.trim(),
      password,
      confirmPassword,
    }),
  });
}

export async function verifyEmail(email, otp) {
  return apiRequest("/auth/verify-email", {
    method: "POST",
    body: JSON.stringify({ email: email.trim(), otp: otp.trim() }),
  });
}

export async function resendOtp(email) {
  return apiRequest("/auth/resend-otp", {
    method: "POST",
    body: JSON.stringify({ email: email.trim() }),
  });
}

export function logout() {
  localStorage.removeItem("healthai_token");
  localStorage.removeItem("healthai_user");
}

export function getCurrentUser() {
  try {
    const raw = localStorage.getItem("healthai_user");
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
}

export function isLoggedIn() {
  return Boolean(localStorage.getItem("healthai_token"));
}
