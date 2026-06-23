"use client";

import { useEffect, useMemo, useState } from "react";
import { Brain, Filter, RefreshCw, Search, Sparkles } from "lucide-react";

import DashboardLayout from "@/components/layout/DashboardLayout";
import Button from "@/components/ui/Button";
import Card from "@/components/ui/Card";
import RecommendationCard from "@/components/recommendations/RecommendationCard";

import { getCurrentUser } from "@/lib/auth";
import { getStudentRecommendations } from "@/lib/student-api";
import { AiRecommendation } from "@/types";

type PriorityFilter = "ALL" | "HIGH" | "MEDIUM" | "LOW";

export default function StudentRecommendationsPage() {
  const [recommendations, setRecommendations] = useState<AiRecommendation[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [searchText, setSearchText] = useState("");
  const [priorityFilter, setPriorityFilter] =
    useState<PriorityFilter>("ALL");

  async function loadRecommendations() {
    const user = getCurrentUser();

    if (!user?.id) {
      setError("Unable to identify the logged-in student.");
      setLoading(false);
      return;
    }

    setLoading(true);
    setError("");

    try {
      const data = await getStudentRecommendations(user.id);
      setRecommendations(data);
    } catch (err) {
      console.error("Failed to load AI recommendations:", err);

      setError(
        "Unable to load AI recommendations. Make sure the backend is running."
      );
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadRecommendations();
  }, []);

  const filteredRecommendations = useMemo(() => {
    return recommendations.filter((recommendation) => {
      const matchesPriority =
        priorityFilter === "ALL" ||
        recommendation.priority === priorityFilter;

      const searchValue = searchText.toLowerCase();

      const matchesSearch =
        recommendation.resourceTitle.toLowerCase().includes(searchValue) ||
        recommendation.weakTopic.toLowerCase().includes(searchValue) ||
        recommendation.courseTitle.toLowerCase().includes(searchValue);

      return matchesPriority && matchesSearch;
    });
  }, [recommendations, priorityFilter, searchText]);

  const highPriorityCount = recommendations.filter(
    (recommendation) => recommendation.priority === "HIGH"
  ).length;

  return (
    <DashboardLayout role="STUDENT">
      <div>
        <div className="flex flex-col justify-between gap-4 md:flex-row md:items-start">
          <div>
            <h2 className="text-2xl font-bold text-gray-900">
              AI Learning Recommendations
            </h2>

            <p className="mt-2 max-w-2xl text-gray-600">
              Personalized trusted resources generated from your performance and
              weak topics.
            </p>
          </div>

          <Button
            variant="outline"
            onClick={loadRecommendations}
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

        <div className="mt-8 grid gap-6 md:grid-cols-2 xl:grid-cols-3">
          <Card>
            <div className="flex items-center gap-3">
              <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-blue-100 text-blue-700">
                <Brain size={22} />
              </div>

              <div>
                <p className="text-sm text-gray-500">Total Resources</p>
                <p className="text-2xl font-bold text-gray-900">
                  {loading ? "..." : recommendations.length}
                </p>
              </div>
            </div>
          </Card>

          <Card>
            <div className="flex items-center gap-3">
              <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-red-100 text-red-700">
                <Sparkles size={22} />
              </div>

              <div>
                <p className="text-sm text-gray-500">High Priority</p>
                <p className="text-2xl font-bold text-gray-900">
                  {loading ? "..." : highPriorityCount}
                </p>
              </div>
            </div>
          </Card>

          <Card>
            <div className="flex items-center gap-3">
              <div className="flex h-11 w-11 items-center justify-center rounded-xl bg-green-100 text-green-700">
                <Filter size={22} />
              </div>

              <div>
                <p className="text-sm text-gray-500">Showing Results</p>
                <p className="text-2xl font-bold text-gray-900">
                  {loading ? "..." : filteredRecommendations.length}
                </p>
              </div>
            </div>
          </Card>
        </div>

        <Card className="mt-8">
          <div className="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
            <div className="relative w-full lg:max-w-md">
              <Search
                size={18}
                className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"
              />

              <input
                value={searchText}
                onChange={(event) => setSearchText(event.target.value)}
                placeholder="Search by course, topic, or resource..."
                className="w-full rounded-lg border border-gray-300 py-2.5 pl-10 pr-4 text-sm outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-100"
              />
            </div>

            <div className="flex flex-wrap gap-2">
              {(["ALL", "HIGH", "MEDIUM", "LOW"] as PriorityFilter[]).map(
                (priority) => (
                  <button
                    key={priority}
                    type="button"
                    onClick={() => setPriorityFilter(priority)}
                    className={`rounded-lg px-4 py-2 text-sm font-semibold transition ${
                      priorityFilter === priority
                        ? "bg-blue-600 text-white"
                        : "bg-gray-100 text-gray-700 hover:bg-gray-200"
                    }`}
                  >
                    {priority === "ALL" ? "All" : `${priority} Priority`}
                  </button>
                )
              )}
            </div>
          </div>
        </Card>

        {loading ? (
          <div className="mt-8 grid gap-6 md:grid-cols-2 xl:grid-cols-3">
            {[1, 2, 3].map((item) => (
              <Card key={item} className="animate-pulse">
                <div className="h-10 w-10 rounded-xl bg-gray-200" />
                <div className="mt-5 h-4 w-1/2 rounded bg-gray-100" />
                <div className="mt-3 h-5 w-3/4 rounded bg-gray-200" />
                <div className="mt-3 h-4 w-full rounded bg-gray-100" />
                <div className="mt-2 h-4 w-5/6 rounded bg-gray-100" />
              </Card>
            ))}
          </div>
        ) : filteredRecommendations.length === 0 ? (
          <Card className="mt-8 text-center">
            <Brain className="mx-auto text-blue-600" size={38} />

            <h3 className="mt-4 text-lg font-bold text-gray-900">
              No recommendations found
            </h3>

            <p className="mx-auto mt-2 max-w-lg text-sm leading-6 text-gray-600">
              Recommendations appear automatically when you score below 70% or
              when your teacher generates learning resources for you.
            </p>
          </Card>
        ) : (
          <div className="mt-8 grid gap-6 md:grid-cols-2 xl:grid-cols-3">
            {filteredRecommendations.map((recommendation) => (
              <RecommendationCard
                key={recommendation.id}
                recommendation={recommendation}
              />
            ))}
          </div>
        )}
      </div>
    </DashboardLayout>
  );
}