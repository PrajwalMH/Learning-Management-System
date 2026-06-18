"use client";

import { FormEvent, useState } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { AxiosError } from "axios";
import { UserPlus } from "lucide-react";

import PublicHeader from "@/components/layout/PublicHeader";
import Footer from "@/components/layout/Footer";
import Button from "@/components/ui/Button";
import Card from "@/components/ui/Card";
import Input from "@/components/ui/Input";
import { getDashboardPath, registerUser } from "@/lib/auth";
import { ApiError, Role } from "@/types";

export default function RegisterPage() {
  const router = useRouter();

  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("password123");
  const [role, setRole] = useState<Role>("STUDENT");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    setError("");
    setLoading(true);

    try {
      const response = await registerUser({
        fullName,
        email,
        password,
        role,
      });

      router.push(getDashboardPath(response.role));
    } catch (err) {
      const axiosError = err as AxiosError<ApiError>;

      setError(
        axiosError.response?.data?.message ||
          "Registration failed. Please try again."
      );
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="min-h-screen bg-gray-50">
      <PublicHeader />

      <section className="mx-auto flex max-w-7xl items-center justify-center px-6 py-16">
        <Card className="w-full max-w-md">
          <div className="mb-8 text-center">
            <div className="mx-auto mb-4 flex h-14 w-14 items-center justify-center rounded-2xl bg-blue-100 text-blue-700">
              <UserPlus size={28} />
            </div>

            <h1 className="text-2xl font-bold text-gray-900">
              Create your LMS account
            </h1>

            <p className="mt-2 text-sm text-gray-600">
              Register as Admin, Teacher, or Student.
            </p>
          </div>

          {error && (
            <div className="mb-4 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <Input
              label="Full Name"
              name="fullName"
              type="text"
              placeholder="Student One"
              value={fullName}
              onChange={(event) => setFullName(event.target.value)}
              required
            />

            <Input
              label="Email"
              name="email"
              type="email"
              placeholder="student1@test.com"
              value={email}
              onChange={(event) => setEmail(event.target.value)}
              required
            />

            <Input
              label="Password"
              name="password"
              type="password"
              placeholder="password123"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              required
            />

            <div>
              <label
                htmlFor="role"
                className="mb-1 block text-sm font-medium text-gray-700"
              >
                Role
              </label>

              <select
                id="role"
                value={role}
                onChange={(event) => setRole(event.target.value as Role)}
                className="w-full rounded-lg border border-gray-300 px-3 py-2 text-sm text-gray-900 shadow-sm outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
              >
                <option value="STUDENT">Student</option>
                <option value="TEACHER">Teacher</option>
                <option value="ADMIN">Admin</option>
              </select>
            </div>

            <Button type="submit" fullWidth disabled={loading}>
              {loading ? "Creating account..." : "Create Account"}
            </Button>
          </form>

          <p className="mt-6 text-center text-sm text-gray-600">
            Already have an account?{" "}
            <Link
              href="/login"
              className="font-semibold text-blue-600 hover:text-blue-700"
            >
              Login
            </Link>
          </p>
        </Card>
      </section>

      <Footer />
    </main>
  );
}