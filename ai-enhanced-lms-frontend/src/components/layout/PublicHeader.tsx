import Link from "next/link";
import Button from "@/components/ui/Button";

export default function PublicHeader() {
  return (
    <header className="border-b border-gray-200 bg-white">
      <div className="mx-auto flex max-w-7xl items-center justify-between px-6 py-4">
        <Link href="/" className="text-xl font-bold text-gray-900">
          AI-Enhanced LMS
        </Link>

        <nav className="flex items-center gap-3">
          <Link href="/login" className="text-sm font-medium text-gray-700 hover:text-blue-600">
            Login
          </Link>

          <Link href="/register">
            <Button>Get Started</Button>
          </Link>
        </nav>
      </div>
    </header>
  );
}