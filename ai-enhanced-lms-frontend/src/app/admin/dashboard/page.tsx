"use client";

import { BookOpen, GraduationCap, Users } from "lucide-react";

import DashboardLayout from "@/components/layout/DashboardLayout";
import StatCard from "@/components/dashboard/StatCard";
import Card from "@/components/ui/Card";

export default function AdminDashboardPage() {
  return (
    <DashboardLayout role="ADMIN">
      <div>
        <h2 className="text-2xl font-bold text-gray-900">Admin Dashboard</h2>
        <p className="mt-2 text-gray-600">
          Manage platform users, courses, teachers, and students.
        </p>

        <div className="mt-8 grid gap-6 md:grid-cols-2 xl:grid-cols-3">
          <StatCard
            title="Total Users"
            value="--"
            description="Students, teachers, and admins"
            icon={<Users size={22} />}
          />

          <StatCard
            title="Active Courses"
            value="--"
            description="Courses currently available"
            icon={<BookOpen size={22} />}
          />

          <StatCard
            title="Total Enrollments"
            value="--"
            description="Student course enrollments"
            icon={<GraduationCap size={22} />}
          />
        </div>

        <Card className="mt-8">
          <h3 className="text-lg font-semibold text-gray-900">
            Platform Overview
          </h3>
          <p className="mt-2 text-sm text-gray-600">
            Admin data and user management APIs will be connected here next.
          </p>
        </Card>
      </div>
    </DashboardLayout>
  );
}