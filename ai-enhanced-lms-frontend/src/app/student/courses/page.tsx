"use client";

import { useEffect, useState } from "react";
import { BookOpen, RefreshCw } from "lucide-react";

import DashboardLayout from "@/components/layout/DashboardLayout";
import Button from "@/components/ui/Button";
import Card from "@/components/ui/Card";
import CourseCard from "@/components/courses/CourseCard";

import { getCurrentUser } from "@/lib/auth";
import {
  getCourseById,
  getStudentEnrollments,
  getStudentProgress,
} from "@/lib/student-api";

import { Course, Enrollment, Progress } from "@/types";

export default function StudentCoursesPage() {
  const [courses, setCourses] = useState<Course[]>([]);
  const [progressRecords, setProgressRecords] = useState<Progress[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  async function loadCourses() {
    const user = getCurrentUser();

    if (!user?.id) {
      setError("Unable to identify the logged-in student.");
      setLoading(false);
      return;
    }

    setLoading(true);
    setError("");

    try {
      const [enrollments, progressData] = await Promise.all([
        getStudentEnrollments(user.id),
        getStudentProgress(user.id),
      ]);

      const activeEnrollments = enrollments.filter(
        (enrollment: Enrollment) => enrollment.active
      );

      const courseData = await Promise.all(
        activeEnrollments.map((enrollment: Enrollment) =>
          getCourseById(enrollment.courseId)
        )
      );

      setCourses(courseData);
      setProgressRecords(progressData);
    } catch (err) {
      console.error("Failed to load student courses:", err);
      setError(
        "Unable to load your courses. Please confirm the backend is running."
      );
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadCourses();
  }, []);

  function findProgress(courseId: number) {
    return progressRecords.find((progress) => progress.courseId === courseId);
  }

  return (
    <DashboardLayout role="STUDENT">
      <div>
        <div className="flex flex-col justify-between gap-4 md:flex-row md:items-start">
          <div>
            <h2 className="text-2xl font-bold text-gray-900">My Courses</h2>

            <p className="mt-2 text-gray-600">
              Continue learning and track your progress in enrolled courses.
            </p>
          </div>

          <Button variant="outline" onClick={loadCourses} disabled={loading}>
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
                <div className="mt-5 h-5 w-3/4 rounded bg-gray-200" />
                <div className="mt-3 h-4 w-full rounded bg-gray-100" />
                <div className="mt-2 h-4 w-5/6 rounded bg-gray-100" />
                <div className="mt-6 h-2 w-full rounded bg-gray-100" />
              </Card>
            ))}
          </div>
        ) : courses.length === 0 ? (
          <Card className="mt-8 text-center">
            <BookOpen className="mx-auto text-blue-600" size={34} />

            <h3 className="mt-4 text-lg font-bold text-gray-900">
              No enrolled courses yet
            </h3>

            <p className="mt-2 text-sm text-gray-600">
              Once an admin enrolls you in a course, it will appear here.
            </p>
          </Card>
        ) : (
          <div className="mt-8 grid gap-6 md:grid-cols-2 xl:grid-cols-3">
            {courses.map((course) => (
              <CourseCard
                key={course.id}
                course={course}
                progress={findProgress(course.id)}
              />
            ))}
          </div>
        )}
      </div>
    </DashboardLayout>
  );
}