import { ReactNode } from "react";
import Card from "@/components/ui/Card";

interface StatCardProps {
  title: string;
  value: string | number;
  description?: string;
  icon: ReactNode;
}

export default function StatCard({
  title,
  value,
  description,
  icon,
}: StatCardProps) {
  return (
    <Card className="flex items-start justify-between">
      <div>
        <p className="text-sm font-medium text-gray-500">{title}</p>
        <h3 className="mt-2 text-3xl font-bold text-gray-900">{value}</h3>

        {description && (
          <p className="mt-2 text-sm text-gray-500">{description}</p>
        )}
      </div>

      <div className="flex h-12 w-12 items-center justify-center rounded-xl bg-blue-100 text-blue-700">
        {icon}
      </div>
    </Card>
  );
}