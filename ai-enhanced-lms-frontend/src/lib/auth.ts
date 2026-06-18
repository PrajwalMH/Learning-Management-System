import api from "@/lib/api";
import { AuthResponse, LoginRequest, RegisterRequest, Role } from "@/types";

const TOKEN_KEY = "token";
const USER_KEY = "user";

export async function loginUser(data: LoginRequest): Promise<AuthResponse> {
  const response = await api.post<AuthResponse>("/auth/login", data);

  saveAuthData(response.data);

  return response.data;
}

export async function registerUser(data: RegisterRequest): Promise<AuthResponse> {
  const response = await api.post<AuthResponse>("/auth/register", data);

  saveAuthData(response.data);

  return response.data;
}

export function saveAuthData(authResponse: AuthResponse) {
  if (typeof window === "undefined") return;

  localStorage.setItem(TOKEN_KEY, authResponse.token);

  const user = {
    id: authResponse.id || authResponse.userId,
    fullName: authResponse.fullName,
    email: authResponse.email,
    role: authResponse.role,
  };

  localStorage.setItem(USER_KEY, JSON.stringify(user));
}

export function getToken(): string | null {
  if (typeof window === "undefined") return null;

  return localStorage.getItem(TOKEN_KEY);
}

export function getCurrentUser(): {
  id?: number;
  fullName?: string;
  email?: string;
  role?: Role;
} | null {
  if (typeof window === "undefined") return null;

  const user = localStorage.getItem(USER_KEY);

  if (!user) return null;

  try {
    return JSON.parse(user);
  } catch {
    return null;
  }
}

export function isAuthenticated(): boolean {
  return !!getToken();
}

export function logoutUser() {
  if (typeof window === "undefined") return;

  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(USER_KEY);

  window.location.href = "/login";
}

export function getDashboardPath(role?: Role): string {
  if (role === "ADMIN") return "/admin/dashboard";
  if (role === "TEACHER") return "/teacher/dashboard";
  if (role === "STUDENT") return "/student/dashboard";

  return "/login";
}