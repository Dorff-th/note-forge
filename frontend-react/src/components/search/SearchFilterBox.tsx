// src/components/search/SearchFilterBox.tsx
import { useEffect, useState } from 'react';
import type { Category } from '@/types/Category';
import { fetchCategories } from '@/api/categoryApi';

interface Props {
  show: boolean;
  onClose?: () => void;
  onSubmit: (condition: {
    categoryId: number | 'all';
    searchFields: string[];
    dateFrom?: string;
    dateTo?: string;
  }) => void;
}

export default function SearchFilterBox({ show, onClose, onSubmit }: Props) {
  const [categories, setCategories] = useState<Category[]>([]);
  const [category, setCategory] = useState<'all' | number>('all');
  const [filters, setFilters] = useState({
    title: false,
    content: false,
    comment: false,
  });
  const [dateRange, setDateRange] = useState({ from: '', to: '' });

  // ✅ 카테고리 불러오기
  useEffect(() => {
    fetchCategories()
      .then((data) => setCategories(data))
      .catch((err) => console.error('카테고리 불러오기 실패:', err));
  }, []);

  const handleReset = () => {
    setCategory('all');
    setFilters({ title: false, content: false, comment: false });
    setDateRange({ from: '', to: '' });
  };

  const handleApply = () => {
    const condition = {
      categoryId: category,
      searchFields: [
        ...(filters.title ? ['title'] : []),
        ...(filters.content ? ['content'] : []),
        ...(filters.comment ? ['comment'] : []),
      ],
      dateFrom: dateRange.from || undefined,
      dateTo: dateRange.to || undefined,
    };
    onSubmit(condition);
    if (onClose) onClose();
  };

  if (!show) return null;

  return (
    <div className="px-6 py-4 bg-gray-50 border-t animate-slide-down">
      {/* 카테고리 */}
      <div className="mb-4">
        <label className="block font-medium mb-1">카테고리</label>
        <select
          value={category}
          onChange={(e) => setCategory(e.target.value === 'all' ? 'all' : Number(e.target.value))}
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

      {/* 버튼 */}
      <div className="flex justify-between">
        <button
          type="button"
          onClick={handleReset}
          className="px-4 py-2 border rounded-md text-gray-600 hover:bg-gray-100 transition"
        >
          초기화
        </button>
        <button
          type="button"
          onClick={handleApply}
          className="px-4 py-2 bg-indigo-500 text-white rounded-md hover:bg-indigo-600 transition"
        >
          적용
        </button>
      </div>
    </div>
  );
}
