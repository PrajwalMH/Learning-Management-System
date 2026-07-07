import api from "@/lib/api";
import {
  AiRecommendation,
  Assignment,
  Course,
  Enrollment,
  Grade,
  Progress,
  QuizAttemptRequest,
  QuizAttemptResponse,
  StudentQuiz,
} from "@/types";


export async function getStudentEnrollments(studentId: number) {
  const response = await api.get<Enrollment[]>(
    `/enrollments/student/${studentId}`
  );

  return response.data;
}


export async function getCourseById(courseId: number) {
  const response = await api.get<Course>(`/courses/${courseId}`);
  return response.data;
}

export async function getStudentProgress(studentId: number) {
  const response = await api.get<Progress[]>(
    `/progress/student/${studentId}`
  );

  return response.data;
}

export async function getStudentGrades(studentId: number) {
  const response = await api.get<Grade[]>(
    `/students/${studentId}/grades`
  );

  return response.data;
}

export async function getStudentRecommendations(studentId: number) {
  const response = await api.get<AiRecommendation[]>(
    `/ai/recommendations/student/${studentId}`
  );

  return response.data;
}

export async function getAssignmentsByCourse(courseId: number) {
  const response = await api.get<Assignment[]>(
    `/courses/${courseId}/assignments`
  );

  return response.data;
}

export async function getPublishedQuizzesByCourse(courseId: number) {
  const response = await api.get<StudentQuiz[]>(
    `/student/quizzes/course/${courseId}`
  );

  return response.data;
}

export async function getStudentQuizById(quizId: number) {
  const response = await api.get<StudentQuiz>(
    `/student/quizzes/${quizId}`
  );

  return response.data;
}

export async function submitQuizAttempt(
  quizId: number,
  data: QuizAttemptRequest
) {
  const response = await api.post<QuizAttemptResponse>(
    `/quizzes/${quizId}/attempt`,
    data
  );

  return response.data;
}

export async function getStudentQuizAttempts(studentId: number) {
  const response = await api.get<QuizAttemptResponse[]>(
    `/students/${studentId}/quiz-attempts`
  );

  return response.data;
}
