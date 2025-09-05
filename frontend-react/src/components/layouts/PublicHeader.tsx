// src/components/layout/PublicHeader.tsx
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { Search, Filter } from 'lucide-react';
import { useSelector, useDispatch } from 'react-redux';
import type { RootState } from '@store/index';
import { setSearchCondition } from '@/store/slices/searchSlice';
import { useState } from 'react';
import SearchFilterBox from '@/components/search/SearchFilterBox';

export default function PublicHeader() {
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useDispatch();

  // ✅ Redux keyword 가져오기 → 로컬에서만 입력 제어
  const globalKeyword = useSelector((state: RootState) => state.search.keyword);
  const [localKeyword, setLocalKeyword] = useState(globalKeyword);

  // 상세조건 토글
  const [showFilters, setShowFilters] = useState(false);

  // ✅ 상세조건 박스에서 "적용" 눌렀을 때
  const handleFilterSubmit = (condition: any) => {
    dispatch(
      setSearchCondition({
        keyword: localKeyword,
        searchFields: [], // ✅ 기본값
        categoryId: 'all',
        dateFrom: undefined,
        dateTo: undefined,
        ...condition, // 상세조건에서 온 값이 있으면 덮어씀
      }),
    );
    if (!location.pathname.startsWith('/search')) {
      navigate(`/search?keyword=${encodeURIComponent(localKeyword)}`);
    }
  };

  // ✅ Enter 검색 실행
  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!localKeyword.trim()) return;

    dispatch(
      setSearchCondition({
        keyword: localKeyword,
        searchFields: [], // ✅ 기본값
        categoryId: 'all',
        dateFrom: undefined,
        dateTo: undefined,
      }),
    );

    if (!location.pathname.startsWith('/search')) {
      navigate(`/search?keyword=${encodeURIComponent(localKeyword)}`);
    }
  };

  return (
    <header className="flex flex-col bg-white shadow-sm border-b">
      {/* 상단 헤더 */}
      <div className="flex items-center justify-between px-6 py-4">
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
          <form onSubmit={handleSubmit} className="relative flex gap-2">
            <div className="relative flex-1">
              <input
                type="text"
                placeholder="Search..."
                value={localKeyword}
                onChange={(e) => setLocalKeyword(e.target.value)}
                className="w-full border rounded-md py-2 px-4 pl-10 
                           focus:outline-none focus:ring-2 
                           focus:ring-indigo-500 focus:border-indigo-500"
              />
              <Search className="absolute left-3 top-2.5 h-5 w-5 text-gray-400" />
            </div>
            <button
              type="submit"
              className="px-4 py-2 bg-indigo-500 text-white rounded-md 
                         hover:bg-indigo-600 transition"
            >
              검색
            </button>
            <button
              type="button"
              onClick={() => setShowFilters(!showFilters)}
              className="px-4 py-2 border rounded-md bg-gray-50 
                         hover:bg-gray-100 transition"
            >
              <Filter size={18} className="inline mr-1" />
              상세조건
            </button>
          </form>

          {/* ✅ 상세조건 박스 */}
          <SearchFilterBox
            show={showFilters}
            onClose={() => setShowFilters(false)}
            onSubmit={handleFilterSubmit}
          />
        </div>

        {/* 우측 로그인/회원가입 */}
        <div className="flex items-center space-x-4">
          <Link to="/login" className="text-gray-600 hover:text-gray-900 transition">
            Log in
          </Link>
          <Link
            to="/register"
            className="px-4 py-2 border border-indigo-500 text-indigo-500 rounded-md 
                       hover:bg-indigo-500 hover:text-white transition"
          >
            Create account
          </Link>
        </div>
      </div>
    </header>
  );
}
