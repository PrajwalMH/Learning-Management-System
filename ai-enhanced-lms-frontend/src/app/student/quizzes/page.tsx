"use client";

import { useEffect, useState } from "react";
import { GraduationCap, RefreshCw } from "lucide-react";

import DashboardLayout from "@/components/layout/DashboardLayout";
import Button from "@/components/ui/Button";
import Card from "@/components/ui/Card";
import QuizCard from "@/components/quizzes/QuizCard";

import { getCurrentUser } from "@/lib/auth";
import {
  getPublishedQuizzesByCourse,
  getStudentEnrollments,
} from "@/lib/student-api";

import { Enrollment, StudentQuiz } from "@/types";

export default function StudentQuizzesPage() {
  const [quizzes, setQuizzes] = useState<StudentQuiz[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  async function loadQuizzes() {
    const user = getCurrentUser();

    if (!user?.id) {
      setError("Unable to identify the logged-in student.");
      setLoading(false);
      return;
    }

    setLoading(true);
    setError("");

    try {
      const enrollments = await getStudentEnrollments(user.id);

      const activeEnrollments = enrollments.filter(
        (enrollment: Enrollment) => enrollment.active
      );

      const quizResults = await Promise.all(
        activeEnrollments.map((enrollment) =>
          getPublishedQuizzesByCourse(enrollment.courseId)
        )
      );

      const allQuizzes = quizResults
        .flat()
        .sort((a, b) => b.id - a.id);

      setQuizzes(allQuizzes);
    } catch (err) {
      console.error("Failed to load quizzes:", err);

      setError(
        "Unable to load quizzes. Make sure your backend is running and quiz APIs are available."
      );
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadQuizzes();
  }, []);

  return (
    <DashboardLayout role="STUDENT">
      <div>
        <div className="flex flex-col justify-between gap-4 md:flex-row md:items-start">
          <div>
            <h2 className="text-2xl font-bold text-gray-900">My Quizzes</h2>

            <p className="mt-2 text-gray-600">
              Attempt published quizzes from your enrolled courses.
            </p>
          </div>

          <Button variant="outline" onClick={loadQuizzes} disabled={loading}>
            <RefreshCw size={17} className={loading ? "animate-spin" : ""} />
            <span className="ml-2">Refresh</span>
          </Button>
        </div>

        {error && (
          <div className="mt-6 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
            {error}
          </div>
        )}

        {loading ? (
          <div className="mt-8 grid gap-6 md:grid-cols-2 xl:grid-cols-3">
            {[1, 2, 3].map((item) => (
              <Card key={item} className="animate-pulse">
                <div className="h-10 w-10 rounded-xl bg-gray-200" />
                <div className="mt-5 h-4 w-1/2 rounded bg-gray-100" />
                <div className="mt-3 h-5 w-3/4 rounded bg-gray-200" />
                <div className="mt-3 h-4 w-full rounded bg-gray-100" />
              </Card>
            ))}
          </div>
        ) : quizzes.length === 0 ? (
          <Card className="mt-8 text-center">
            <GraduationCap className="mx-auto text-blue-600" size={38} />

            <h3 className="mt-4 text-lg font-bold text-gray-900">
              No published quizzes yet
            </h3>

            <p className="mt-2 text-sm text-gray-600">
              Published quizzes from your enrolled courses will appear here.
            </p>
          </Card>
        ) : (
          <div className="mt-8 grid gap-6 md:grid-cols-2 xl:grid-cols-3">
            {quizzes.map((quiz) => (
              <QuizCard key={quiz.id} quiz={quiz} />
            ))}
          </div>
        )}
      </div>
    </DashboardLayout>
  );
}