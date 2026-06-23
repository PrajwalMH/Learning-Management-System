import { CalendarDays, ClipboardList, FileText } from "lucide-react";

import Card from "@/components/ui/Card";
import { Assignment } from "@/types";

interface AssignmentCardProps {
  assignment: Assignment;
  courseTitle?: string;
}

export default function AssignmentCard({
  assignment,
  courseTitle,
}: AssignmentCardProps) {
  const dueDate = assignment.dueDate
    ? new Date(assignment.dueDate).toLocaleString()
    : "No due date";

  const isPastDue =
    assignment.dueDate && new Date(assignment.dueDate) < new Date();

  return (
    <Card className="flex h-full flex-col">
      <div className="flex items-start justify-between gap-4">
        <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-blue-100 text-blue-700">
          <ClipboardList size={21} />
        </div>

        <span
          className={`rounded-full px-3 py-1 text-xs font-semibold ${
            assignment.published
              ? "bg-green-100 text-green-700"
              : "bg-gray-100 text-gray-600"
          }`}
        >
          {assignment.published ? "PUBLISHED" : "DRAFT"}
        </span>
      </div>

      <p className="mt-5 text-xs font-semibold uppercase tracking-wide text-blue-600">
        {courseTitle || assignment.courseTitle || "Course Assignment"}
      </p>

      <h3 className="mt-2 text-lg font-bold text-gray-900">
        {assignment.title}
      </h3>

      <p className="mt-2 line-clamp-3 text-sm leading-6 text-gray-600">
        {assignment.description || "No assignment description available."}
      </p>

      <div className="mt-5 space-y-2 text-sm text-gray-600">
        <div className="flex items-center gap-2">
          <FileText size={16} />
          <span>Maximum marks: {assignment.maxMarks}</span>
        </div>

        <div
          className={`flex items-center gap-2 ${
            isPastDue ? "text-red-600" : ""
          }`}
        >
          <CalendarDays size={16} />
          <span>Due: {dueDate}</span>
        </div>
      </div>

      <button
        type="button"
        className="mt-6 rounded-lg bg-blue-600 px-4 py-2 text-sm font-semibold text-white transition hover:bg-blue-700"
      >
        View Assignment
      </button>
    </Card>
  );
}