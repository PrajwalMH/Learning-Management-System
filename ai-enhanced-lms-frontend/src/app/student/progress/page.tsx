"use client";

import { useEffect, useMemo, useState } from "react";
import { BarChart3, RefreshCw, TrendingUp } from "lucide-react";
import {
  Bar,
  BarChart,
  CartesianGrid,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from "recharts";

import DashboardLayout from "@/components/layout/DashboardLayout";
import Button from "@/components/ui/Button";
import Card from "@/components/ui/Card";
import StatCard from "@/components/dashboard/StatCard";
import ProgressCard from "@/components/progress/ProgressCard";

import { getCurrentUser } from "@/lib/auth";
import { getStudentProgress } from "@/lib/student-api";
import { Progress } from "@/types";

export default function StudentProgressPage() {
  const [progressRecords, setProgressRecords] = useState<Progress[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  async function loadProgress() {
    const user = getCurrentUser();

    if (!user?.id) {
      setError("Unable to identify the logged-in student.");
      setLoading(false);
      return;
    }

    setLoading(true);
    setError("");

    try {
      const data = await getStudentProgress(user.id);
      setProgressRecords(data);
    } catch (err) {
      console.error("Failed to load progress:", err);

      setError(
        "Unable to load progress data. Make sure your backend is running."
      );
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadProgress();
  }, []);

  const averageProgress = useMemo(() => {
    if (progressRecords.length === 0) return 0;

    const total = progressRecords.reduce(
      (sum, progress) => sum + (progress.progressPercentage || 0),
      0
    );

    return Math.round(total / progressRecords.length);
  }, [progressRecords]);

  const averageScore = useMemo(() => {
    if (progressRecords.length === 0) return 0;

    const total = progressRecords.reduce(
      (sum, progress) => sum + (progress.averageScore || 0),
      0
    );

    return Math.round(total / progressRecords.length);
  }, [progressRecords]);

  const totalCompletedModules = useMemo(() => {
    return progressRecords.reduce(
      (sum, progress) => sum + (progress.completedModules || 0),
      0
    );
  }, [progressRecords]);

  const chartData = useMemo(() => {
    return progressRecords.map((progress) => ({
      course:
        progress.courseTitle.length > 18
          ? `${progress.courseTitle.slice(0, 18)}...`
          : progress.courseTitle,
      progress: Math.round(progress.progressPercentage || 0),
      score: Math.round(progress.averageScore || 0),
    }));
  }, [progressRecords]);

  return (
    <DashboardLayout role="STUDENT">
      <div>
        <div className="flex flex-col justify-between gap-4 md:flex-row md:items-start">
          <div>
            <h2 className="text-2xl font-bold text-gray-900">
              Learning Progress
            </h2>

            <p className="mt-2 text-gray-600">
              Track your course completion, academic performance, and progress
              across enrolled courses.
            </p>
          </div>

          <Button variant="outline" onClick={loadProgress} disabled={loading}>
            <RefreshCw size={17} className={loading ? "animate-spin" : ""} />
            <span className="ml-2">Refresh</span>
          </Button>
        </div>

        {error && (
          <div className="mt-6 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
            {error}
          </div>
        )}

        <div className="mt-8 grid gap-6 md:grid-cols-2 xl:grid-cols-3">
          <StatCard
            title="Average Progress"
            value={loading ? "..." : `${averageProgress}%`}
            description="Across your enrolled courses"
            icon={<TrendingUp size={22} />}
          />

          <StatCard
            title="Average Score"
            value={loading ? "..." : `${averageScore}%`}
            description="Based on graded work"
            icon={<BarChart3 size={22} />}
          />

          <StatCard
            title="Modules Completed"
            value={loading ? "..." : totalCompletedModules}
            description="Total completed learning modules"
            icon={<TrendingUp size={22} />}
          />
        </div>

        <Card className="mt-8">
          <div>
            <h3 className="text-lg font-semibold text-gray-900">
              Course Performance Overview
            </h3>

            <p className="mt-1 text-sm text-gray-600">
              Compare your completion progress and average score by course.
            </p>
          </div>

          <div className="mt-6 h-80">
            {loading ? (
              <div className="flex h-full items-center justify-center">
                <p className="text-sm text-gray-500">Loading chart...</p>
              </div>
            ) : chartData.length === 0 ? (
              <div className="flex h-full items-center justify-center">
                <p className="text-sm text-gray-500">
                  No course progress data available yet.
                </p>
              </div>
            ) : (
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={chartData}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="course" />
                  <YAxis domain={[0, 100]} />
                  <Tooltip />
                  <Bar dataKey="progress" name="Progress %" radius={[6, 6, 0, 0]} />
                  <Bar dataKey="score" name="Average Score %" radius={[6, 6, 0, 0]} />
                </BarChart>
              </ResponsiveContainer>
            )}
          </div>
        </Card>

        <section className="mt-8">
          <h3 className="text-xl font-bold text-gray-900">
            Course-by-Course Progress
          </h3>

          <p className="mt-1 text-sm text-gray-600">
            Detailed progress and performance for each enrolled course.
          </p>

          {loading ? (
            <div className="mt-6 grid gap-6 md:grid-cols-2 xl:grid-cols-3">
              {[1, 2, 3].map((item) => (
                <Card key={item} className="animate-pulse">
                  <div className="h-10 w-10 rounded-xl bg-gray-200" />
                  <div className="mt-5 h-5 w-3/4 rounded bg-gray-200" />
                  <div className="mt-6 h-3 w-full rounded bg-gray-100" />
                  <div className="mt-6 h-20 rounded-xl bg-gray-100" />
                </Card>
              ))}
            </div>
          ) : progressRecords.length === 0 ? (
            <Card className="mt-6 text-center">
              <BarChart3 className="mx-auto text-blue-600" size={34} />

              <h4 className="mt-4 text-lg font-bold text-gray-900">
                No progress data yet
              </h4>

              <p className="mt-2 text-sm text-gray-600">
                Progress will appear after you enroll in a course and start
                completing modules or receiving grades.
              </p>
            </Card>
          ) : (
            <div className="mt-6 grid gap-6 md:grid-cols-2 xl:grid-cols-3">
              {progressRecords.map((progress) => (
                <ProgressCard key={progress.id} progress={progress} />
              ))}
            </div>
          )}
        </section>
      </div>
    </DashboardLayout>
  );
}