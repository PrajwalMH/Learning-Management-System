export default function Footer() {
  return (
    <footer className="border-t border-gray-200 bg-white">
      <div className="mx-auto flex max-w-7xl flex-col items-center justify-between gap-2 px-6 py-5 text-sm text-gray-500 md:flex-row">
        <p>© {new Date().getFullYear()} AI-Enhanced LMS. All rights reserved.</p>
        <p>Built with Spring Boot, MySQL, Next.js, and AI-powered learning.</p>
      </div>
    </footer>
  );
}