import { ExternalLink, Sparkles } from "lucide-react";
import Card from "@/components/ui/Card";
import { AiRecommendation } from "@/types";

interface RecommendationCardProps {
  recommendation: AiRecommendation;
}

export default function RecommendationCard({
  recommendation,
}: RecommendationCardProps) {
  const priorityClasses = {
    HIGH: "bg-red-100 text-red-700",
    MEDIUM: "bg-amber-100 text-amber-700",
    LOW: "bg-green-100 text-green-700",
  };

  return (
    <Card className="flex h-full flex-col">
      <div className="flex items-start justify-between gap-4">
        <div className="flex items-center gap-2">
          <div className="flex h-9 w-9 items-center justify-center rounded-lg bg-blue-100 text-blue-700">
            <Sparkles size={18} />
          </div>

          <span
            className={`rounded-full px-2.5 py-1 text-xs font-semibold ${
              priorityClasses[recommendation.priority]
            }`}
          >
            {recommendation.priority} PRIORITY
          </span>
        </div>

        <span className="text-xs font-medium text-gray-500">
          Score: {recommendation.recommendationScore}
        </span>
      </div>

      <p className="mt-4 text-xs font-semibold uppercase tracking-wide text-gray-500">
        Weak Topic: {recommendation.weakTopic}
      </p>

      <h3 className="mt-2 text-lg font-semibold text-gray-900">
        {recommendation.resourceTitle}
      </h3>

      <p className="mt-2 line-clamp-3 text-sm leading-6 text-gray-600">
        {recommendation.recommendationText ||
          "Recommended learning resource based on your recent performance."}
      </p>

      <div className="mt-4 flex items-center justify-between gap-3">
        <span className="rounded-md bg-gray-100 px-2 py-1 text-xs font-medium text-gray-600">
          {recommendation.resourceType.replaceAll("_", " ")}
        </span>

        <a
          href={recommendation.resourceUrl}
          target="_blank"
          rel="noreferrer"
          className="inline-flex items-center gap-1 text-sm font-semibold text-blue-600 hover:text-blue-700"
        >
          Open Resource
          <ExternalLink size={15} />
        </a>
      </div>
    </Card>
  );
}