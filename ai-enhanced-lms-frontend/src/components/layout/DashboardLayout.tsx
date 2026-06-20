"use client";

import { ReactNode, useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { Menu } from "lucide-react";

import Sidebar from "@/components/layout/Sidebar";
import Button from "@/components/ui/Button";
import { getCurrentUser, getDashboardPath, isAuthenticated } from "@/lib/auth";
import { Role } from "@/types";

interface DashboardLayoutProps {
  role: Role;
  children: ReactNode;
}

export default function DashboardLayout({
  role,
  children,
}: DashboardLayoutProps) {
  const router = useRouter();

  const [userName, setUserName] = useState("");
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [isCheckingAuth, setIsCheckingAuth] = useState(true);

  useEffect(() => {
    if (!isAuthenticated()) {
      router.replace("/login");
      return;
    }

    const user = getCurrentUser();

    if (!user?.role) {
      router.replace("/login");
      return;
    }

    if (user.role !== role) {
      router.replace(getDashboardPath(user.role));
      return;
    }

    setUserName(user.fullName || "User");
    setIsCheckingAuth(false);
  }, [role, router]);

  if (isCheckingAuth) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-gray-50">
        <p className="text-sm font-medium text-gray-600">
          Loading dashboard...
        </p>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Desktop Sidebar */}
      <div className="fixed inset-y-0 left-0 z-30 hidden lg:block">
        <Sidebar role={role} userName={userName} />
      </div>

      {/* Mobile Sidebar */}
      {isSidebarOpen && (
        <div className="fixed inset-0 z-50 lg:hidden">
          <div
            className="absolute inset-0 bg-black/40"
            onClick={() => setIsSidebarOpen(false)}
          />

          <div className="relative z-10 h-full">
            <Sidebar role={role} userName={userName} />
          </div>
        </div>
      )}

      {/* Main Area */}
      <div className="min-h-screen lg:ml-72">
        <header className="sticky top-0 z-20 border-b border-gray-200 bg-white px-6 py-4">
          <div className="flex items-center gap-4">
            <Button
              type="button"
              variant="outline"
              className="lg:hidden"
              onClick={() => setIsSidebarOpen(true)}
            >
              <Menu size={18} />
            </Button>

            <div>
              <p className="text-sm text-gray-500">Welcome back,</p>
              <h1 className="text-lg font-bold text-gray-900">{userName}</h1>
            </div>
          </div>
        </header>

        <main className="min-h-[calc(100vh-73px)] p-6 md:p-8">
          {children}
        </main>
      </div>
    </div>
  );
}