import { BookOpen, ChevronRight, User } from "lucide-react";
import Link from "next/link";

import Card from "@/components/ui/Card";
import { Course, Progress } from "@/types";

interface CourseCardProps {
  course: Course;
  progress?: Progress;
}

export default function CourseCard({
  course,
  progress,
}: CourseCardProps) {
  const percentage = Math.round(progress?.progressPercentage || 0);
  const averageScore = Math.round(progress?.averageScore || 0);

  return (
    <Card className="flex h-full flex-col">
      <div className="flex items-start justify-between gap-4">
        <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-blue-100 text-blue-700">
          <BookOpen size={21} />
        </div>

        <span className="rounded-full bg-gray-100 px-3 py-1 text-xs font-semibold text-gray-600">
          {course.level || "General"}
        </span>
      </div>

      <h3 className="mt-5 text-lg font-bold text-gray-900">
        {course.title}
      </h3>

      <p className="mt-2 line-clamp-3 text-sm leading-6 text-gray-600">
        {course.description || "No course description available."}
      </p>

      <div className="mt-5 flex items-center gap-2 text-sm text-gray-500">
        <User size={16} />
        <span>{course.teacherName || "Course Instructor"}</span>
      </div>

      <div className="mt-6">
        <div className="flex items-center justify-between text-sm">
          <span className="font-medium text-gray-700">Course Progress</span>
          <span className="font-bold text-blue-600">{percentage}%</span>
        </div>

        <div className="mt-2 h-2 overflow-hidden rounded-full bg-gray-100">
          <div
            className="h-full rounded-full bg-blue-600 transition-all"
            style={{ width: `${Math.min(Math.max(percentage, 0), 100)}%` }}
          />
        </div>

        <p className="mt-2 text-xs text-gray-500">
          Average score: {averageScore}%
        </p>
      </div>

      <Link
        href={`/student/courses/${course.id}`}
        className="mt-6 inline-flex items-center justify-center gap-2 rounded-lg border border-blue-200 px-4 py-2 text-sm font-semibold text-blue-600 transition hover:bg-blue-50"
      >
        Open Course
        <ChevronRight size={16} />
      </Link>
    </Card>
  );
}