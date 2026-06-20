"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import {
  BarChart3,
  BookOpen,
  Brain,
  ClipboardList,
  GraduationCap,
  LayoutDashboard,
  LogOut,
  MessageSquare,
  Settings,
  Users,
} from "lucide-react";

import { Role } from "@/types";
import { logoutUser } from "@/lib/auth";

interface SidebarProps {
  role: Role;
  userName?: string;
}

interface MenuItem {
  label: string;
  href: string;
  icon: React.ReactNode;
}

export default function Sidebar({ role, userName }: SidebarProps) {
  const pathname = usePathname();

  const menuItems = getMenuItems(role);

  return (
    <aside className="flex min-h-screen w-72 flex-col border-r border-gray-200 bg-white">
      <div className="border-b border-gray-200 px-6 py-5">
        <Link href="/" className="text-xl font-bold text-gray-900">
          AI-Enhanced LMS
        </Link>

        <p className="mt-1 text-xs font-medium uppercase tracking-wide text-blue-600">
          {role} Portal
        </p>
      </div>

      <div className="border-b border-gray-100 px-6 py-4">
        <p className="text-sm font-semibold text-gray-900">
          {userName || "User"}
        </p>
        <p className="mt-1 text-xs text-gray-500">{role}</p>
      </div>

      <nav className="flex-1 space-y-1 px-4 py-5">
        {menuItems.map((item) => {
          const isActive = pathname === item.href;

          return (
            <Link
              key={item.href}
              href={item.href}
              className={`flex items-center gap-3 rounded-lg px-4 py-3 text-sm font-medium transition ${
                isActive
                  ? "bg-blue-600 text-white"
                  : "text-gray-600 hover:bg-gray-100 hover:text-gray-900"
              }`}
            >
              {item.icon}
              {item.label}
            </Link>
          );
        })}
      </nav>

      <div className="border-t border-gray-200 p-4">
        <button
          onClick={logoutUser}
          className="flex w-full items-center gap-3 rounded-lg px-4 py-3 text-sm font-medium text-red-600 transition hover:bg-red-50"
        >
          <LogOut size={18} />
          Logout
        </button>
      </div>
    </aside>
  );
}

function getMenuItems(role: Role): MenuItem[] {
  if (role === "ADMIN") {
    return [
      {
        label: "Dashboard",
        href: "/admin/dashboard",
        icon: <LayoutDashboard size={18} />,
      },
      {
        label: "Users",
        href: "/admin/users",
        icon: <Users size={18} />,
      },
      {
        label: "Courses",
        href: "/admin/courses",
        icon: <BookOpen size={18} />,
      },
      {
        label: "Settings",
        href: "/admin/settings",
        icon: <Settings size={18} />,
      },
    ];
  }

  if (role === "TEACHER") {
    return [
      {
        label: "Dashboard",
        href: "/teacher/dashboard",
        icon: <LayoutDashboard size={18} />,
      },
      {
        label: "My Courses",
        href: "/teacher/courses",
        icon: <BookOpen size={18} />,
      },
      {
        label: "Assignments",
        href: "/teacher/assignments",
        icon: <ClipboardList size={18} />,
      },
      {
        label: "Analytics",
        href: "/teacher/analytics",
        icon: <BarChart3 size={18} />,
      },
      {
        label: "Discussions",
        href: "/teacher/discussions",
        icon: <MessageSquare size={18} />,
      },
    ];
  }

  return [
    {
      label: "Dashboard",
      href: "/student/dashboard",
      icon: <LayoutDashboard size={18} />,
    },
    {
      label: "My Courses",
      href: "/student/courses",
      icon: <BookOpen size={18} />,
    },
    {
      label: "Assignments",
      href: "/student/assignments",
      icon: <ClipboardList size={18} />,
    },
    {
      label: "Progress",
      href: "/student/progress",
      icon: <BarChart3 size={18} />,
    },
    {
      label: "AI Recommendations",
      href: "/student/recommendations",
      icon: <Brain size={18} />,
    },
    {
      label: "Quizzes",
      href: "/student/quizzes",
      icon: <GraduationCap size={18} />,
    },
    {
      label: "Discussions",
      href: "/student/discussions",
      icon: <MessageSquare size={18} />,
    },
  ];
}