"use client";

import { useEffect, useState } from "react";
import { ClipboardList, RefreshCw } from "lucide-react";

import DashboardLayout from "@/components/layout/DashboardLayout";
import AssignmentCard from "@/components/assignments/AssignmentCard";
import Button from "@/components/ui/Button";
import Card from "@/components/ui/Card";

import { getCurrentUser } from "@/lib/auth";
import {
  getAssignmentsByCourse,
  getCourseById,
  getStudentEnrollments,
} from "@/lib/student-api";

import { Assignment, Course, Enrollment } from "@/types";

interface AssignmentWithCourse extends Assignment {
  displayCourseTitle?: string;
}

export default function StudentAssignmentsPage() {
  const [assignments, setAssignments] = useState<AssignmentWithCourse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  async function loadAssignments() {
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

      const courseResults = await Promise.all(
        activeEnrollments.map((enrollment) =>
          getCourseById(enrollment.courseId)
        )
      );

      const assignmentResults = await Promise.all(
        courseResults.map(async (course: Course) => {
          const courseAssignments = await getAssignmentsByCourse(course.id);

          return courseAssignments
            .filter((assignment) => assignment.published)
            .map((assignment) => ({
              ...assignment,
              displayCourseTitle: course.title,
            }));
        })
      );

      const allAssignments = assignmentResults
        .flat()
        .sort(
          (a, b) =>
            new Date(a.dueDate).getTime() - new Date(b.dueDate).getTime()
        );

      setAssignments(allAssignments);
    } catch (err) {
      console.error("Failed to load assignments:", err);

      setError(
        "Unable to load assignments. Confirm that the backend is running and your API endpoints are available."
      );
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadAssignments();
  }, []);

  return (
    <DashboardLayout role="STUDENT">
      <div>
        <div className="flex flex-col justify-between gap-4 md:flex-row md:items-start">
          <div>
            <h2 className="text-2xl font-bold text-gray-900">
              My Assignments
            </h2>

            <p className="mt-2 text-gray-600">
              Review published assignments from your enrolled courses.
            </p>
          </div>

          <Button
            variant="outline"
            onClick={loadAssignments}
            disabled={loading}
          >
            <RefreshCw
              size={17}
              className={loading ? "animate-spin" : ""}
            />
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
                <div className="mt-5 h-4 w-1/3 rounded bg-gray-100" />
                <div className="mt-3 h-5 w-3/4 rounded bg-gray-200" />
                <div className="mt-3 h-4 w-full rounded bg-gray-100" />
                <div className="mt-2 h-4 w-4/5 rounded bg-gray-100" />
              </Card>
            ))}
          </div>
        ) : assignments.length === 0 ? (
          <Card className="mt-8 text-center">
            <ClipboardList className="mx-auto text-blue-600" size={34} />

            <h3 className="mt-4 text-lg font-bold text-gray-900">
              No published assignments
            </h3>

            <p className="mt-2 text-sm text-gray-600">
              Published assignments from your enrolled courses will appear here.
            </p>
          </Card>
        ) : (
          <div className="mt-8 grid gap-6 md:grid-cols-2 xl:grid-cols-3">
            {assignments.map((assignment) => (
              <AssignmentCard
                key={assignment.id}
                assignment={assignment}
                courseTitle={assignment.displayCourseTitle}
              />
            ))}
          </div>
        )}
      </div>
    </DashboardLayout>
  );
}