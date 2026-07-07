"use client";

import { useEffect, useMemo, useState } from "react";
import Link from "next/link";
import { AxiosError } from "axios";
import { useParams } from "next/navigation";
import {
  ArrowLeft,
  CheckCircle2,
  CircleAlert,
  ClipboardCheck,
  Loader2,
} from "lucide-react";

import DashboardLayout from "@/components/layout/DashboardLayout";
import Button from "@/components/ui/Button";
import Card from "@/components/ui/Card";

import { getCurrentUser } from "@/lib/auth";
import {
  getStudentQuizById,
  getStudentQuizAttempts,
  submitQuizAttempt,
} from "@/lib/student-api";

import { QuizAttemptResponse, StudentQuiz } from "@/types";

export default function StudentQuizAttemptPage() {
  const params = useParams();
  const quizId = Number(params.quizId);

  const [quiz, setQuiz] = useState<StudentQuiz | null>(null);
  const [answers, setAnswers] = useState<Record<number, string>>({});
  const [result, setResult] = useState<QuizAttemptResponse | null>(null);

  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  async function loadQuiz() {
    if (!quizId || Number.isNaN(quizId)) {
      setError("Invalid quiz ID.");
      setLoading(false);
      return;
    }

    const user = getCurrentUser();

    if (!user?.id) {
      setError("Unable to identify the logged-in student.");
      setLoading(false);
      return;
    }

    setLoading(true);
    setError("");

    try {
      const [quizData, studentAttempts] = await Promise.all([
        getStudentQuizById(quizId),
        getStudentQuizAttempts(user.id),
      ]);

      setQuiz(quizData);

      const existingAttempt = studentAttempts.find(
        (attempt) => attempt.quizId === quizId
      );

      if (existingAttempt) {
        setResult(existingAttempt);
      }
    } catch (err) {
      console.error("Failed to load quiz:", err);

      const axiosError = err as AxiosError<{
        message?: string;
        error?: string;
      }>;

      setError(
        axiosError.response?.data?.message ||
          axiosError.response?.data?.error ||
          "Unable to load this quiz. It may not be published or may no longer exist."
      );
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadQuiz();
  }, [quizId]);

  const totalQuestions = quiz?.questions?.length || 0;

  const answeredQuestions = useMemo(() => {
    return Object.keys(answers).length;
  }, [answers]);

  function selectAnswer(questionId: number, answer: string) {
    if (result) return;

    setAnswers((previous) => ({
      ...previous,
      [questionId]: answer,
    }));
  }

  async function handleSubmitQuiz() {
    const user = getCurrentUser();

    if (!user?.id) {
      setError("Unable to identify the logged-in student.");
      return;
    }

    if (!quiz) return;

    if (answeredQuestions !== totalQuestions) {
      setError(
        `Please answer all ${totalQuestions} questions before submitting.`
      );
      return;
    }

    const shouldSubmit = window.confirm(
      "Are you sure you want to submit this quiz? You can attempt it only once."
    );

    if (!shouldSubmit) return;

    setSubmitting(true);
    setError("");

    try {
      const response = await submitQuizAttempt(quiz.id, {
        studentId: user.id,
        submittedAnswers: answers,
      });

      setResult(response);
    } catch (err) {
      const axiosError = err as AxiosError<{
        message?: string;
        error?: string;
      }>;

      console.error(
        "Quiz submission failed:",
        axiosError.response?.data || axiosError.message
      );

      const backendMessage =
        axiosError.response?.data?.message ||
        axiosError.response?.data?.error ||
        "Unable to submit your quiz. Please try again.";

      setError(backendMessage);

      if (
        backendMessage.toLowerCase().includes("already attempted")
      ) {
        loadQuiz();
      }
    } finally {
      setSubmitting(false);
    }
  }

  if (loading) {
    return (
      <DashboardLayout role="STUDENT">
        <div className="flex min-h-[60vh] items-center justify-center">
          <div className="flex items-center gap-3 text-sm font-medium text-gray-600">
            <Loader2 size={20} className="animate-spin" />
            Loading quiz...
          </div>
        </div>
      </DashboardLayout>
    );
  }

  if (error && !quiz) {
    return (
      <DashboardLayout role="STUDENT">
        <Card className="mx-auto max-w-2xl text-center">
          <CircleAlert className="mx-auto text-red-600" size={38} />

          <h2 className="mt-4 text-xl font-bold text-gray-900">
            Quiz Unavailable
          </h2>

          <p className="mt-2 text-sm text-gray-600">{error}</p>

          <Link
            href="/student/quizzes"
            className="mt-6 inline-flex rounded-lg bg-blue-600 px-4 py-2 text-sm font-semibold text-white hover:bg-blue-700"
          >
            Back to Quizzes
          </Link>
        </Card>
      </DashboardLayout>
    );
  }

  if (!quiz) return null;

  if (result) {
    return (
      <DashboardLayout role="STUDENT">
        <div className="mx-auto max-w-3xl">
          <Card className="text-center">
            <div className="mx-auto flex h-16 w-16 items-center justify-center rounded-full bg-green-100 text-green-700">
              <CheckCircle2 size={34} />
            </div>

            <h2 className="mt-5 text-2xl font-bold text-gray-900">
              Quiz Already Submitted
            </h2>

            <p className="mt-2 text-sm text-gray-600">
              Your previous quiz result is shown below.
            </p>

            <div className="mt-8 grid gap-4 sm:grid-cols-3">
              <ResultBox label="Score" value={`${result.score}`} />

              <ResultBox
                label="Correct Answers"
                value={`${result.correctAnswers}/${result.totalQuestions}`}
              />

              <ResultBox
                label="Percentage"
                value={`${Math.round(result.percentage)}%`}
              />
            </div>

            <div className="mt-8 flex flex-col justify-center gap-3 sm:flex-row">
              <Link
                href="/student/quizzes"
                className="rounded-lg bg-blue-600 px-4 py-2 text-sm font-semibold text-white hover:bg-blue-700"
              >
                Back to Quizzes
              </Link>

              <Link
                href="/student/dashboard"
                className="rounded-lg border border-gray-300 px-4 py-2 text-sm font-semibold text-gray-700 hover:bg-gray-50"
              >
                Go to Dashboard
              </Link>
            </div>
          </Card>
        </div>
      </DashboardLayout>
    );
  }

  return (
    <DashboardLayout role="STUDENT">
      <div className="mx-auto max-w-4xl">
        <Link
          href="/student/quizzes"
          className="inline-flex items-center gap-2 text-sm font-semibold text-blue-600 hover:text-blue-700"
        >
          <ArrowLeft size={17} />
          Back to Quizzes
        </Link>

        <Card className="mt-5">
          <div className="flex flex-col justify-between gap-4 md:flex-row md:items-start">
            <div>
              <p className="text-xs font-semibold uppercase tracking-wide text-blue-600">
                {quiz.courseTitle}
              </p>

              <h1 className="mt-2 text-2xl font-bold text-gray-900">
                {quiz.title}
              </h1>

              <p className="mt-2 text-sm text-gray-600">
                {quiz.description || "Answer every question before submitting."}
              </p>
            </div>

            <div className="rounded-xl bg-blue-50 px-4 py-3 text-center">
              <p className="text-xs font-medium text-blue-700">Progress</p>

              <p className="mt-1 text-lg font-bold text-blue-900">
                {answeredQuestions}/{totalQuestions}
              </p>
            </div>
          </div>
        </Card>

        {error && (
          <div className="mt-6 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
            {error}
          </div>
        )}

        <div className="mt-6 space-y-6">
          {quiz.questions?.map((question, index) => {
            const choices = [
              { key: "A", text: question.optionA },
              { key: "B", text: question.optionB },
              { key: "C", text: question.optionC },
              { key: "D", text: question.optionD },
            ];

            return (
              <Card key={question.id}>
                <div className="flex items-start gap-3">
                  <span className="flex h-8 w-8 shrink-0 items-center justify-center rounded-full bg-blue-100 text-sm font-bold text-blue-700">
                    {index + 1}
                  </span>

                  <div className="flex-1">
                    <h2 className="text-base font-semibold text-gray-900">
                      {question.questionText}
                    </h2>

                    <div className="mt-5 grid gap-3">
                      {choices.map((choice) => {
                        const isSelected =
                          answers[question.id] === choice.key;

                        return (
                          <button
                            key={choice.key}
                            type="button"
                            onClick={() =>
                              selectAnswer(question.id, choice.key)
                            }
                            className={`flex w-full items-center gap-3 rounded-xl border p-4 text-left text-sm transition ${
                              isSelected
                                ? "border-blue-600 bg-blue-50 text-blue-900"
                                : "border-gray-200 bg-white text-gray-700 hover:border-blue-300 hover:bg-blue-50"
                            }`}
                          >
                            <span
                              className={`flex h-7 w-7 shrink-0 items-center justify-center rounded-full border text-xs font-bold ${
                                isSelected
                                  ? "border-blue-600 bg-blue-600 text-white"
                                  : "border-gray-300 text-gray-600"
                              }`}
                            >
                              {choice.key}
                            </span>

                            <span>{choice.text}</span>
                          </button>
                        );
                      })}
                    </div>
                  </div>
                </div>
              </Card>
            );
          })}
        </div>

        <Card className="sticky bottom-4 mt-8">
          <div className="flex flex-col justify-between gap-4 sm:flex-row sm:items-center">
            <div className="flex items-center gap-3">
              <ClipboardCheck className="text-blue-600" size={21} />

              <div>
                <p className="text-sm font-semibold text-gray-900">
                  Ready to submit?
                </p>

                <p className="text-xs text-gray-500">
                  {answeredQuestions} of {totalQuestions} questions answered.
                </p>
              </div>
            </div>

            <Button
              type="button"
              onClick={handleSubmitQuiz}
              disabled={submitting || answeredQuestions !== totalQuestions}
            >
              {submitting ? "Submitting..." : "Submit Quiz"}
            </Button>
          </div>
        </Card>
      </div>
    </DashboardLayout>
  );
}

function ResultBox({
  label,
  value,
}: {
  label: string;
  value: string;
}) {
  return (
    <div className="rounded-xl bg-gray-50 p-4">
      <p className="text-xs font-medium uppercase tracking-wide text-gray-500">
        {label}
      </p>

      <p className="mt-2 text-2xl font-bold text-gray-900">{value}</p>
    </div>
  );
}