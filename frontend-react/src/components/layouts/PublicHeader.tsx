// src/components/layout/PublicHeader.tsx
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { Search, Filter } from 'lucide-react';
import { useSelector, useDispatch } from 'react-redux';
import type { RootState } from '@store/index';
import { setSearchCondition } from '@/store/slices/searchSlice';
import { useState, useEffect } from 'react';
import type { Category } from '@/types/Category';
import { fetchCategories } from '@/api/postApi';

export default function PublicHeader() {
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useDispatch();

  // ✅ Redux에는 최종 검색어만 저장, 로컬 상태로 관리
  const globalKeyword = useSelector((state: RootState) => state.search.keyword);
  const [localKeyword, setLocalKeyword] = useState(globalKeyword);

  // 상세조건 토글 & 상태
  const [showFilters, setShowFilters] = useState(false);
  const [category, setCategory] = useState('all');
  const [categories, setCategories] = useState<Category[]>([]); // DB 카테고리 목록
  const [filters, setFilters] = useState({
    title: false,
    content: false,
    comment: false,
  });
  const [dateRange, setDateRange] = useState({ from: '', to: '' });

  // ✅ 카테고리 목록 불러오기
  useEffect(() => {
    fetchCategories()
      .then((data) => setCategories(data))
      .catch((err) => console.error('카테고리 불러오기 실패:', err));
  }, []);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!localKeyword.trim()) return;

    // ✅ Enter 눌렀을 때만 Redux 상태 업데이트
    dispatch(
      setSearchCondition({
        keyword: localKeyword,
        categoryId: category === 'all' ? 'all' : Number(category),
        searchFields: [
          ...(filters.title ? ['title'] : []),
          ...(filters.content ? ['content'] : []),
          ...(filters.comment ? ['comment'] : []),
        ],
        dateFrom: dateRange.from || undefined,
        dateTo: dateRange.to || undefined,
      }),
    );

    if (!location.pathname.startsWith('/search')) {
      navigate(`/search?keyword=${encodeURIComponent(localKeyword)}`);
    }
  };

  const handleReset = () => {
    setCategory('all');
    setFilters({ title: false, content: false, comment: false });
    setDateRange({ from: '', to: '' });
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
                onChange={(e) => setLocalKeyword(e.target.value)} // ✅ 로컬 상태만 갱신
                className="w-full border rounded-md py-2 px-4 pl-10 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500"
              />
              <Search className="absolute left-3 top-2.5 h-5 w-5 text-gray-400" />
            </div>
            <button
              type="submit"
              className="px-4 py-2 bg-indigo-500 text-white rounded-md hover:bg-indigo-600 transition"
            >
              검색
            </button>
            <button
              type="button"
              onClick={() => setShowFilters(!showFilters)}
              className="px-4 py-2 border rounded-md bg-gray-50 hover:bg-gray-100 transition"
            >
              <Filter size={18} className="inline mr-1" />
              상세조건
            </button>
          </form>
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
      </div>

      {/* 상세조건 패널 */}
      {showFilters && (
        <div className="px-6 py-4 bg-gray-50 border-t animate-slide-down">
          {/* 카테고리 */}
          <div className="mb-4">
            <label className="block font-medium mb-1">카테고리</label>
            <select
              value={category}
              onChange={(e) => setCategory(e.target.value)}
              className="px-3 py-2 border rounded-md w-full"
            >
              <option value="all">전체</option>
              {categories.map((cat) => (
                <option key={cat.id} value={cat.id.toString()}>
                  {cat.name}
                </option>
              ))}
            </select>
          </div>

          {/* 체크박스 */}
          <div className="mb-4">
            <label className="block font-medium mb-1">검색 범위</label>
            <div className="flex gap-6">
              <label className="flex items-center gap-2">
                <input
                  type="checkbox"
                  checked={filters.title}
                  onChange={(e) => setFilters({ ...filters, title: e.target.checked })}
                />
                제목
              </label>
              <label className="flex items-center gap-2">
                <input
                  type="checkbox"
                  checked={filters.content}
                  onChange={(e) => setFilters({ ...filters, content: e.target.checked })}
                />
                내용
              </label>
              <label className="flex items-center gap-2">
                <input
                  type="checkbox"
                  checked={filters.comment}
                  onChange={(e) => setFilters({ ...filters, comment: e.target.checked })}
                />
                댓글
              </label>
            </div>
          </div>

          {/* 날짜 범위 */}
          <div className="mb-4">
            <label className="block font-medium mb-1">날짜 범위</label>
            <div className="flex items-center gap-2">
              <input
                type="date"
                value={dateRange.from}
                onChange={(e) => setDateRange({ ...dateRange, from: e.target.value })}
                className="px-3 py-2 border rounded-md"
              />
              ~
              <input
                type="date"
                value={dateRange.to}
                onChange={(e) => setDateRange({ ...dateRange, to: e.target.value })}
                className="px-3 py-2 border rounded-md"
              />
            </div>
          </div>

          {/* 초기화 버튼 */}
          <div className="flex justify-end">
            <button
              type="button"
              onClick={handleReset}
              className="px-4 py-2 border rounded-md text-gray-600 hover:bg-gray-100 transition"
            >
              초기화
            </button>
          </div>
        </div>
      )}
    </header>
  );
}
