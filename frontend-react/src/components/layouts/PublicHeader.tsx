// src/components/layout/PublicHeader.tsx
import { Link } from 'react-router-dom';
import { Search } from 'lucide-react';

export default function PublicHeader() {
  return (
    <header className="flex items-center justify-between px-6 py-4 bg-white shadow-sm">
      {/* 좌측 로고 */}
      <div className="flex items-center space-x-6">
        <Link to="/" className="text-xl font-bold text-gray-800">
          MKHub
        </Link>
        <Link to="/posts" className="text-gray-600 hover:text-gray-900 transition">
          Blog-list
        </Link>
      </div>

      {/* 중앙 검색 */}
      <div className="flex-1 max-w-lg mx-6">
        <div className="relative">
          <input
            type="text"
            placeholder="Search..."
            className="w-full border rounded-md py-2 px-4 pl-10 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
          />
          <Search className="absolute left-3 top-2.5 h-5 w-5 text-gray-400" />
        </div>
      </div>

      {/* 우측 로그인/회원가입 */}
      <div className="flex items-center space-x-4">
        <Link to="/login" className="text-gray-600 hover:text-gray-900 transition">
          Log in
        </Link>
        <Link
          to="/register"
          className="px-4 py-2 border border-indigo-500 text-indigo-500 rounded-md hover:bg-indigo-500 hover:text-white transition"
        >
          Create account
        </Link>
      </div>
    </header>
  );
}
