// src/components/layout/UserHeader.tsx
import { Link, useNavigate } from 'react-router-dom';
import { Search, LogOut } from 'lucide-react';
import { useDispatch, useSelector } from 'react-redux';
import { logout } from '@store/slices/authSlice';
import type { RootState } from '@store/index';
import { setKeyword } from '@/store/slices/searchSlice';
import Avatar from '@/components/common/Avatar';

export default function UserHeader() {
  const dispatch = useDispatch();
  const user = useSelector((state: RootState) => state.auth.user);

  const keyword = useSelector((state: RootState) => state.search.keyword);

  const navigate = useNavigate();

  const handleLogout = () => {
    dispatch(logout());
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!keyword.trim()) return;

    dispatch(setKeyword(keyword));
    navigate(`/search?keyword=${encodeURIComponent(keyword)}`);
  };

  return (
    <header className="flex items-center justify-between px-6 py-4 bg-white shadow-sm">
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
        <div className="relative">
          <form onSubmit={handleSubmit}>
            <input
              type="text"
              placeholder="Search..."
              value={keyword}
              onChange={(e) => dispatch(setKeyword(e.target.value))}
              className="w-full border rounded-md py-2 px-4 pl-10 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
            />
            <Search className="absolute left-3 top-2.5 h-5 w-5 text-gray-400" />
          </form>
        </div>
      </div>

      {/* 우측: 글쓰기, 프로필, 로그아웃 */}
      <div className="flex items-center space-x-4">
        {/* <Link
          to="/posts/new"
          className="flex items-center space-x-1 px-4 py-2 border border-indigo-500 text-indigo-500 rounded-md hover:bg-indigo-500 hover:text-white transition"
        >
          <PlusCircle className="h-4 w-4" />
          <span>New Post</span>
        </Link> */}

        <div className="flex items-center space-x-2">
          <Avatar src={user?.profileImageUrl} alt={user?.nickname} size={32} />
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
    </header>
  );
}
