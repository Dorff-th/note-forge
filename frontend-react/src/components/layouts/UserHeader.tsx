// src/components/layout/UserHeader.tsx
import { Link, useNavigate, Outlet } from 'react-router-dom';
import { Search, Filter, LogOut } from 'lucide-react';
import { useDispatch, useSelector } from 'react-redux';
import { logout } from '@store/slices/authSlice';
import type { RootState } from '@store/index';
import { setSearchCondition } from '@/store/slices/searchSlice';
import Avatar from '@/components/common/Avatar';
import { useState } from 'react';
import SearchFilterBox from '@/components/search/SearchFilterBox';

export default function UserHeader() {
  const dispatch = useDispatch();
  const user = useSelector((state: RootState) => state.auth.user);
  const globalKeyword = useSelector((state: RootState) => state.search.keyword);

  const [localKeyword, setLocalKeyword] = useState(globalKeyword);
  const [showFilters, setShowFilters] = useState(false);

  const navigate = useNavigate();

  const handleLogout = () => {
    dispatch(logout());
  };

  const handleFilterSubmit = (condition: any) => {
    dispatch(
      setSearchCondition({
        keyword: localKeyword,
        searchFields: [],
        categoryId: 'all',
        dateFrom: undefined,
        dateTo: undefined,
        ...condition,
      }),
    );
    if (!location.pathname.startsWith('/search')) {
      navigate(`/search?keyword=${encodeURIComponent(localKeyword)}`);
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!localKeyword.trim()) return;

    dispatch(
      setSearchCondition({
        keyword: localKeyword,
        searchFields: [],
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
    <div className="flex flex-col min-h-screen bg-gray-50">
      {/* 🔹 헤더 */}
      <header className="flex flex-col bg-white shadow-sm border-b">
        <div className="flex items-center justify-between px-6 py-4">
          {/* 좌측 로고 & 메뉴 */}
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

            <SearchFilterBox
              show={showFilters}
              onClose={() => setShowFilters(false)}
              onSubmit={handleFilterSubmit}
            />
          </div>

          {/* 우측: 프로필, 로그아웃 */}
          <div className="flex items-center space-x-4">
            <div className="flex items-center space-x-2">
              <Link to="/user/profile">
                <Avatar src={user?.profileImageUrl} alt={user?.nickname} size={32} />
              </Link>
              <span className="text-gray-700 font-medium">{user?.nickname ?? 'User'}</span>
            </div>

            <button
              onClick={handleLogout}
              className="flex items-center space-x-1 text-gray-600 hover:text-red-500 transition"
            >
              <LogOut className="h-5 w-5" />
              <span>Logout</span>
            </button>
          </div>
        </div>
      </header>

      {/* 🔹 자식 라우트 출력 자리 */}
      <main className="flex-1 p-6">
        <Outlet />
      </main>
    </div>
  );
}
