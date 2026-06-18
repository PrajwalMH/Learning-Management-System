"use client";

import { FormEvent, useState } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { AxiosError } from "axios";
import { Brain } from "lucide-react";

import PublicHeader from "@/components/layout/PublicHeader";
import Footer from "@/components/layout/Footer";
import Button from "@/components/ui/Button";
import Card from "@/components/ui/Card";
import Input from "@/components/ui/Input";
import { getDashboardPath, loginUser } from "@/lib/auth";
import { ApiError } from "@/types";

export default function LoginPage() {
  const router = useRouter();

  const [email, setEmail] = useState("teacher@test.com");
  const [password, setPassword] = useState("password123");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    setError("");
    setLoading(true);

    try {
      const response = await loginUser({
        email,
        password,
      });

      router.push(getDashboardPath(response.role));
    } catch (err) {
      const axiosError = err as AxiosError<ApiError>;

      setError(
        axiosError.response?.data?.message ||
          "Login failed. Please check your email and password."
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
              <Brain size={28} />
            </div>

            <h1 className="text-2xl font-bold text-gray-900">
              Login to AI-Enhanced LMS
            </h1>

            <p className="mt-2 text-sm text-gray-600">
              Access your role-based dashboard.
            </p>
          </div>

          {error && (
            <div className="mb-4 rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <Input
              label="Email"
              name="email"
              type="email"
              placeholder="teacher@test.com"
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

            <Button type="submit" fullWidth disabled={loading}>
              {loading ? "Logging in..." : "Login"}
            </Button>
          </form>

          <p className="mt-6 text-center text-sm text-gray-600">
            New user?{" "}
            <Link
              href="/register"
              className="font-semibold text-blue-600 hover:text-blue-700"
            >
              Create an account
            </Link>
          </p>
        </Card>
      </section>

      <Footer />
    </main>
  );
}