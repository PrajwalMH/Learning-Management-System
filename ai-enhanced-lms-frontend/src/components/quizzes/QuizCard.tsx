import Link from "next/link";
import {
  Brain,
  CheckCircle2,
  ClipboardCheck,
  Clock,
  PlayCircle,
} from "lucide-react";

import Card from "@/components/ui/Card";
import { QuizAttemptResponse, StudentQuiz } from "@/types";

interface QuizCardProps {
  quiz: StudentQuiz;
  attempt?: QuizAttemptResponse;
}

export default function QuizCard({ quiz, attempt }: QuizCardProps) {
  const isSubmitted = !!attempt;

  return (
    <Card className="flex h-full flex-col">
      <div className="flex items-start justify-between gap-4">
        <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-blue-100 text-blue-700">
          <Brain size={21} />
        </div>

        {isSubmitted ? (
          <span className="inline-flex items-center gap-1 rounded-full bg-green-100 px-3 py-1 text-xs font-semibold text-green-700">
            <CheckCircle2 size={13} />
            SUBMITTED
          </span>
        ) : (
          <span className="rounded-full bg-blue-100 px-3 py-1 text-xs font-semibold text-blue-700">
            AVAILABLE
          </span>
        )}
      </div>

      <p className="mt-5 text-xs font-semibold uppercase tracking-wide text-blue-600">
        {quiz.courseTitle}
      </p>

      <h3 className="mt-2 text-lg font-bold text-gray-900">{quiz.title}</h3>

      <p className="mt-2 line-clamp-3 text-sm leading-6 text-gray-600">
        {quiz.description || "No quiz description available."}
      </p>

      <div className="mt-5 space-y-2 text-sm text-gray-600">
        <div className="flex items-center gap-2">
          <ClipboardCheck size={16} />
          <span>{quiz.totalQuestions} questions</span>
        </div>

        <div className="flex items-center gap-2">
          <Clock size={16} />
          <span>Topic: {quiz.topic}</span>
        </div>
      </div>

      {isSubmitted ? (
        <div className="mt-6 rounded-xl bg-green-50 p-4">
          <p className="text-sm font-semibold text-green-800">
            Quiz already submitted
          </p>

          <p className="mt-1 text-sm text-green-700">
            Score: {attempt.score}/{attempt.totalQuestions} ·{" "}
            {Math.round(attempt.percentage)}%
          </p>
        </div>
      ) : (
        <Link
          href={`/student/quizzes/${quiz.id}`}
          className="mt-6 inline-flex items-center justify-center gap-2 rounded-lg bg-blue-600 px-4 py-2 text-sm font-semibold text-white transition hover:bg-blue-700"
        >
          Start Quiz
          <PlayCircle size={17} />
        </Link>
      )}
    </Card>
  );
}