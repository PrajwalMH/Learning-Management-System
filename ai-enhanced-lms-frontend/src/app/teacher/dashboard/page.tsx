"use client";

import { BarChart3, BookOpen, ClipboardList, Users } from "lucide-react";

import DashboardLayout from "@/components/layout/DashboardLayout";
import StatCard from "@/components/dashboard/StatCard";
import Card from "@/components/ui/Card";

export default function TeacherDashboardPage() {
  return (
    <DashboardLayout role="TEACHER">
      <div>
        <h2 className="text-2xl font-bold text-gray-900">Teacher Dashboard</h2>

        <p className="mt-2 text-gray-600">
          Manage courses, assignments, grading, and student performance.
        </p>

        <div className="mt-8 grid gap-6 md:grid-cols-2 xl:grid-cols-4">
          <StatCard
            title="My Courses"
            value="--"
            description="Courses you teach"
            icon={<BookOpen size={22} />}
          />

          <StatCard
            title="Assignments"
            value="--"
            description="Created assignments"
            icon={<ClipboardList size={22} />}
          />

          <StatCard
            title="Students"
            value="--"
            description="Students enrolled"
            icon={<Users size={22} />}
          />

          <StatCard
            title="Class Average"
            value="--"
            description="Average student performance"
            icon={<BarChart3 size={22} />}
          />
        </div>

        <Card className="mt-8">
          <h3 className="text-lg font-semibold text-gray-900">
            Teacher Quick Actions
          </h3>

          <p className="mt-2 text-sm text-gray-600">
            Course creation, assignment management, grading, analytics, and AI
            quiz generation will be added here.
          </p>
        </Card>
      </div>
    </DashboardLayout>
  );
}