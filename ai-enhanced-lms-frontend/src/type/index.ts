export type Role = "ADMIN" | "TEACHER" | "STUDENT";

export interface User {
  id: number;
  fullName: string;
  email: string;
  role: Role;
  enabled: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface AuthResponse {
  token: string;
  id?: number;
  userId?: number;
  fullName?: string;
  email?: string;
  role?: Role;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  fullName: string;
  email: string;
  password: string;
  role: Role;
}

export interface Course {
  id: number;
  title: string;
  description: string;
  category: string;
  level: string;
  active: boolean;
  teacherId?: number;
  teacherName?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface Assignment {
  id: number;
  title: string;
  description: string;
  maxMarks: number;
  dueDate: string;
  published: boolean;
  courseId: number;
  courseTitle?: string;
}

export interface Grade {
  id: number;
  marksObtained: number;
  maxMarks: number;
  percentage: number;
  feedback: string;
  assignmentId: number;
  assignmentTitle: string;
  courseId: number;
  courseTitle: string;
  studentId: number;
  studentName: string;
  studentEmail: string;
  gradedById: number;
  gradedByName: string;
  gradedAt: string;
}

export interface AiRecommendation {
  id: number;
  studentId: number;
  studentName: string;
  courseId: number;
  courseTitle: string;
  weakTopic: string;
  score: number;
  priority: "HIGH" | "MEDIUM" | "LOW";
  searchQuery: string;
  resourceTitle: string;
  recommendationText: string;
  resourceUrl: string;
  resourceType: string;
  recommendationScore: number;
  completed: boolean;
  generatedAt: string;
}

export interface Progress {
  id: number;
  studentId: number;
  studentName: string;
  studentEmail: string;
  courseId: number;
  courseTitle: string;
  completedModules: number;
  totalModules: number;
  progressPercentage: number;
  averageScore: number;
  performanceLevel: string;
  lastAccessedAt: string;
}

export interface ApiError {
  timestamp?: string;
  status?: number;
  error?: string;
  message?: string;
  path?: string;
  validationErrors?: Record<string, string>;
}