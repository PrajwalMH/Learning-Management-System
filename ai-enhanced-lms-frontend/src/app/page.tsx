import Link from "next/link";
import { Brain, BarChart3, GraduationCap, ShieldCheck } from "lucide-react";
import PublicHeader from "@/components/layout/PublicHeader";
import Footer from "@/components/layout/Footer";
import Button from "@/components/ui/Button";
import Card from "@/components/ui/Card";

export default function HomePage() {
  return (
    <main className="min-h-screen bg-gray-50">
      <PublicHeader />

      <section className="mx-auto grid max-w-7xl gap-10 px-6 py-20 lg:grid-cols-2 lg:items-center">
        <div>
          <p className="mb-3 inline-flex rounded-full bg-blue-50 px-4 py-1 text-sm font-medium text-blue-700">
            AI-Powered Learning Management System
          </p>

          <h1 className="text-4xl font-bold tracking-tight text-gray-900 md:text-6xl">
            Smarter learning with analytics, quizzes, and AI recommendations.
          </h1>

          <p className="mt-6 max-w-2xl text-lg leading-8 text-gray-600">
            A full-stack LMS platform for admins, teachers, and students. Manage
            courses, assignments, grades, progress, discussions, notifications,
            quizzes, and personalized AI learning recommendations.
          </p>

          <div className="mt-8 flex flex-col gap-3 sm:flex-row">
            <Link href="/register">
              <Button>Start Learning</Button>
            </Link>

            <Link href="/login">
              <Button variant="outline">Login</Button>
            </Link>
          </div>
        </div>

        <Card className="bg-gradient-to-br from-white to-blue-50">
          <div className="grid gap-4">
            <FeatureRow
              icon={<GraduationCap />}
              title="Role-Based LMS"
              description="Admin, Teacher, and Student dashboards with clean workflows."
            />
            <FeatureRow
              icon={<Brain />}
              title="AI Recommendations"
              description="Low scores trigger trusted learning resources using SerpAPI."
            />
            <FeatureRow
              icon={<BarChart3 />}
              title="Performance Analytics"
              description="Grade trends, progress tracking, histograms, and insights."
            />
            <FeatureRow
              icon={<ShieldCheck />}
              title="Secure Backend"
              description="Spring Boot APIs, JWT authentication, MySQL, and validation."
            />
          </div>
        </Card>
      </section>

      <section className="mx-auto grid max-w-7xl gap-6 px-6 pb-20 md:grid-cols-3">
        <Card>
          <h3 className="text-lg font-semibold text-gray-900">For Admins</h3>
          <p className="mt-2 text-sm leading-6 text-gray-600">
            Manage users, courses, teachers, students, and system-level data.
          </p>
        </Card>

        <Card>
          <h3 className="text-lg font-semibold text-gray-900">For Teachers</h3>
          <p className="mt-2 text-sm leading-6 text-gray-600">
            Create courses, upload resources, grade submissions, and view
            student analytics.
          </p>
        </Card>

        <Card>
          <h3 className="text-lg font-semibold text-gray-900">For Students</h3>
          <p className="mt-2 text-sm leading-6 text-gray-600">
            Access courses, submit assignments, track progress, attempt quizzes,
            and receive AI recommendations.
          </p>
        </Card>
      </section>

      <Footer />
    </main>
  );
}

function FeatureRow({
  icon,
  title,
  description,
}: {
  icon: React.ReactNode;
  title: string;
  description: string;
}) {
  return (
    <div className="flex gap-4 rounded-xl border border-gray-100 bg-white p-4 shadow-sm">
      <div className="flex h-11 w-11 items-center justify-center rounded-lg bg-blue-100 text-blue-700">
        {icon}
      </div>

      <div>
        <h3 className="font-semibold text-gray-900">{title}</h3>
        <p className="mt-1 text-sm leading-6 text-gray-600">{description}</p>
      </div>
    </div>
  );
}