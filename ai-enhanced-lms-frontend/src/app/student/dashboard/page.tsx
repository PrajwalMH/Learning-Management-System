"use client";

import { useEffect, useMemo, useState } from "react";
import {
  BarChart3,
  BookOpen,
  Brain,
  ClipboardList,
  RefreshCw,
} from "lucide-react";

import DashboardLayout from "@/components/layout/DashboardLayout";
import StatCard from "@/components/dashboard/StatCard";
import Card from "@/components/ui/Card";
import Button from "@/components/ui/Button";
import RecommendationCard from "@/components/recommendations/RecommendationCard";

import { getCurrentUser } from "@/lib/auth";
import {
  getStudentEnrollments,
  getStudentGrades,
  getStudentProgress,
  getStudentRecommendations,
} from "@/lib/student-api";

import {
  AiRecommendation,
  Enrollment,
  Grade,
  Progress,
} from "@/types";

export default function StudentDashboardPage() {
  const [enrollments, setEnrollments] = useState<Enrollment[]>([]);
  const [progressRecords, setProgressRecords] = useState<Progress[]>([]);
  const [grades, setGrades] = useState<Grade[]>([]);
  const [recommendations, setRecommendations] = useState<AiRecommendation[]>(
    []
  );

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  async function loadDashboardData() {
    const user = getCurrentUser();

    if (!user?.id) {
      setError("Unable to identify the logged-in student.");
      setLoading(false);
      return;
    }

    setLoading(true);
    setError("");

    try {
      const [enrollmentData, progressData, gradeData, recommendationData] =
        await Promise.all([
          getStudentEnrollments(user.id),
          getStudentProgress(user.id),
          getStudentGrades(user.id),
          getStudentRecommendations(user.id),
        ]);

      setEnrollments(enrollmentData);
      setProgressRecords(progressData);
      setGrades(gradeData);
      setRecommendations(recommendationData);
    } catch (err) {
      console.error("Student dashboard loading failed:", err);
      setError(
        "Unable to load dashboard data. Make sure your Spring Boot backend is running."
      );
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadDashboardData();
  }, []);

  const averageProgress = useMemo(() => {
    if (progressRecords.length === 0) return 0;

    const total = progressRecords.reduce(
      (sum, item) => sum + (item.progressPercentage || 0),
      0
    );

    return Math.round(total / progressRecords.length);
  }, [progressRecords]);

  const averageScore = useMemo(() => {
    if (grades.length === 0) return 0;

    const total = grades.reduce(
      (sum, item) => sum + (item.percentage || 0),
      0
    );

    return Math.round(total / grades.length);
  }, [grades]);

  const pendingAssignments = useMemo(() => {
    return grades.filter((grade) => grade.percentage < 70).length;
  }, [grades]);

  const recentRecommendations = recommendations.slice(0, 3);

  return (
    <DashboardLayout role="STUDENT">
      <div>
        <div className="flex flex-col justify-between gap-4 md:flex-row md:items-start">
          <div>
            <h2 className="text-2xl font-bold text-gray-900">
              Student Dashboard
            </h2>

            <p className="mt-2 text-gray-600">
              Track your learning progress, grades, and personalized AI guidance.
            </p>
          </div>

          <Button
            variant="outline"
            onClick={loadDashboardData}
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

        <div className="mt-8 grid gap-6 md:grid-cols-2 xl:grid-cols-4">
          <StatCard
            title="Enrolled Courses"
            value={loading ? "..." : enrollments.length}
            description="Courses currently enrolled"
            icon={<BookOpen size={22} />}
          />

          <StatCard
            title="Average Progress"
            value={loading ? "..." : `${averageProgress}%`}
            description="Across all enrolled courses"
            icon={<BarChart3 size={22} />}
          />

          <StatCard
            title="Average Score"
            value={loading ? "..." : `${averageScore}%`}
            description="Based on graded submissions"
            icon={<ClipboardList size={22} />}
          />

          <StatCard
            title="AI Recommendations"
            value={loading ? "..." : recommendations.length}
            description="Learning resources for improvement"
            icon={<Brain size={22} />}
          />
        </div>

        <div className="mt-8 grid gap-6 xl:grid-cols-2">
          <Card>
            <div className="flex items-center justify-between">
              <div>
                <h3 className="text-lg font-semibold text-gray-900">
                  My Course Progress
                </h3>

                <p className="mt-1 text-sm text-gray-600">
                  Your current completion status by course.
                </p>
              </div>
            </div>

            <div className="mt-6 space-y-5">
              {loading ? (
                <p className="text-sm text-gray-500">Loading progress...</p>
              ) : progressRecords.length === 0 ? (
                <p className="text-sm text-gray-500">
                  No progress data is available yet.
                </p>
              ) : (
                progressRecords.slice(0, 4).map((progress) => (
                  <div key={progress.id}>
                    <div className="flex items-center justify-between gap-4">
                      <p className="truncate text-sm font-semibold text-gray-900">
                        {progress.courseTitle}
                      </p>

                      <span className="text-sm font-semibold text-blue-600">
                        {Math.round(progress.progressPercentage)}%
                      </span>
                    </div>

                    <div className="mt-2 h-2 overflow-hidden rounded-full bg-gray-100">
                      <div
                        className="h-full rounded-full bg-blue-600"
                        style={{
                          width: `${Math.min(
                            Math.max(progress.progressPercentage || 0, 0),
                            100
                          )}%`,
                        }}
                      />
                    </div>

                    <p className="mt-2 text-xs text-gray-500">
                      Average Score: {Math.round(progress.averageScore || 0)}%
                      {" · "}
                      {progress.performanceLevel.replaceAll("_", " ")}
                    </p>
                  </div>
                ))
              )}
            </div>
          </Card>

          <Card>
            <h3 className="text-lg font-semibold text-gray-900">
              Learning Alert
            </h3>

            <p className="mt-1 text-sm text-gray-600">
              Personalized support based on your recent performance.
            </p>

            <div className="mt-6 rounded-xl bg-blue-50 p-5">
              <p className="text-sm font-semibold text-blue-900">
                {pendingAssignments > 0
                  ? `${pendingAssignments} assessment result${
                      pendingAssignments > 1 ? "s" : ""
                    } need attention.`
                  : "Your current performance is on track."}
              </p>

              <p className="mt-2 text-sm leading-6 text-blue-800">
                Scores below 70% can trigger AI-generated learning resources to
                help you improve weak topics.
              </p>
            </div>
          </Card>
        </div>

        <section className="mt-8">
          <div className="flex items-center justify-between">
            <div>
              <h3 className="text-xl font-bold text-gray-900">
                AI Learning Recommendations
              </h3>

              <p className="mt-1 text-sm text-gray-600">
                Trusted learning resources selected for your weak topics.
              </p>
            </div>
          </div>

          {loading ? (
            <p className="mt-6 text-sm text-gray-500">
              Loading recommendations...
            </p>
          ) : recentRecommendations.length === 0 ? (
            <Card className="mt-6">
              <p className="text-sm text-gray-600">
                No AI recommendations yet. Recommendations appear automatically
                after a score below 70%, or when your teacher generates them.
              </p>
            </Card>
          ) : (
            <div className="mt-6 grid gap-6 lg:grid-cols-3">
              {recentRecommendations.map((recommendation) => (
                <RecommendationCard
                  key={recommendation.id}
                  recommendation={recommendation}
                />
              ))}
            </div>
          )}
        </section>
      </div>
    </DashboardLayout>
  );
}