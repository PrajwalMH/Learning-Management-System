"use client";

import { BarChart3, BookOpen, Brain, ClipboardList } from "lucide-react";

import DashboardLayout from "@/components/layout/DashboardLayout";
import StatCard from "@/components/dashboard/StatCard";
import Card from "@/components/ui/Card";

export default function StudentDashboardPage() {
  return (
    <DashboardLayout role="STUDENT">
      <div>
        <h2 className="text-2xl font-bold text-gray-900">Student Dashboard</h2>
        <p className="mt-2 text-gray-600">
          Track your courses, assignments, grades, progress, and AI learning
          recommendations.
        </p>

        <div className="mt-8 grid gap-6 md:grid-cols-2 xl:grid-cols-4">
          <StatCard
            title="Enrolled Courses"
            value="--"
            description="Courses currently enrolled"
            icon={<BookOpen size={22} />}
          />

          <StatCard
            title="Pending Assignments"
            value="--"
            description="Assignments awaiting submission"
            icon={<ClipboardList size={22} />}
          />

          <StatCard
            title="Average Score"
            value="--"
            description="Overall academic performance"
            icon={<BarChart3 size={22} />}
          />

          <StatCard
            title="AI Recommendations"
            value="--"
            description="Learning resources generated for you"
            icon={<Brain size={22} />}
          />
        </div>

        <div className="mt-8 grid gap-6 xl:grid-cols-2">
          <Card>
            <h3 className="text-lg font-semibold text-gray-900">
              Continue Learning
            </h3>

            <p className="mt-2 text-sm text-gray-600">
              Enrolled courses and module progress will appear here.
            </p>
          </Card>

          <Card>
            <h3 className="text-lg font-semibold text-gray-900">
              AI Learning Recommendations
            </h3>

            <p className="mt-2 text-sm text-gray-600">
              Resources generated after low scores will appear here.
            </p>
          </Card>
        </div>
      </div>
    </DashboardLayout>
  );
}