import { Award, BarChart3, BookOpen } from "lucide-react";

import Card from "@/components/ui/Card";
import { Progress } from "@/types";

interface ProgressCardProps {
  progress: Progress;
}

export default function ProgressCard({ progress }: ProgressCardProps) {
  const progressPercentage = Math.round(progress.progressPercentage || 0);
  const averageScore = Math.round(progress.averageScore || 0);

  const performanceClasses: Record<string, string> = {
    EXCELLENT: "bg-green-100 text-green-700",
    GOOD: "bg-blue-100 text-blue-700",
    AVERAGE: "bg-amber-100 text-amber-700",
    NEEDS_IMPROVEMENT: "bg-orange-100 text-orange-700",
    AT_RISK: "bg-red-100 text-red-700",
    NOT_STARTED: "bg-gray-100 text-gray-700",
  };

  const performanceLabel = progress.performanceLevel
    ? progress.performanceLevel.replaceAll("_", " ")
    : "NOT STARTED";

  return (
    <Card>
      <div className="flex items-start justify-between gap-4">
        <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-blue-100 text-blue-700">
          <BookOpen size={21} />
        </div>

        <span
          className={`rounded-full px-3 py-1 text-xs font-semibold ${
            performanceClasses[progress.performanceLevel] ||
            "bg-gray-100 text-gray-700"
          }`}
        >
          {performanceLabel}
        </span>
      </div>

      <h3 className="mt-5 text-lg font-bold text-gray-900">
        {progress.courseTitle}
      </h3>

      <div className="mt-6">
        <div className="flex items-center justify-between text-sm">
          <span className="font-medium text-gray-700">Course Progress</span>
          <span className="font-bold text-blue-600">
            {progressPercentage}%
          </span>
        </div>

        <div className="mt-2 h-2.5 overflow-hidden rounded-full bg-gray-100">
          <div
            className="h-full rounded-full bg-blue-600 transition-all"
            style={{
              width: `${Math.min(Math.max(progressPercentage, 0), 100)}%`,
            }}
          />
        </div>

        <div className="mt-5 grid grid-cols-2 gap-4">
          <div className="rounded-xl bg-gray-50 p-3">
            <div className="flex items-center gap-2 text-xs text-gray-500">
              <BarChart3 size={15} />
              Average Score
            </div>

            <p className="mt-2 text-xl font-bold text-gray-900">
              {averageScore}%
            </p>
          </div>

          <div className="rounded-xl bg-gray-50 p-3">
            <div className="flex items-center gap-2 text-xs text-gray-500">
              <Award size={15} />
              Modules Done
            </div>

            <p className="mt-2 text-xl font-bold text-gray-900">
              {progress.completedModules || 0}/{progress.totalModules || 0}
            </p>
          </div>
        </div>
      </div>
    </Card>
  );
}