import { useState } from 'react';
import type { SearchFilter } from '@/types/SearchFilter';

interface Category {
  id: number;
  name: string;
}

interface PostSearchFilterProps {
  categories: Category[];
  filter: SearchFilter;
  onChange: (filter: SearchFilter) => void;
  onSearch: () => void;
}

export default function PostSearchFilter({
  categories,
  filter,
  onChange,
  onSearch,
}: PostSearchFilterProps) {
  const [localKeyword, setLocalKeyword] = useState(filter.keyword || '');

  const handleFieldToggle = (field: string) => {
    const exists = filter.searchFields?.includes(field);
    onChange({
      ...filter,
      searchFields: exists
        ? filter.searchFields?.filter((f) => f !== field) || []
        : [...(filter.searchFields || []), field],
    });
  };

  return (
    <div className="mb-6 p-4 border rounded bg-white shadow-sm">
      {/* 검색어 입력 */}
      <form
        onSubmit={(e) => {
          e.preventDefault();
          onChange({ ...filter, keyword: localKeyword });
          onSearch();
        }}
        className="flex gap-2 mb-4"
      >
        <input
          type="text"
          placeholder="검색어를 입력하세요"
          value={localKeyword}
          onChange={(e) => setLocalKeyword(e.target.value)}
          className="flex-1 border rounded px-3 py-2"
        />
        <button
          type="submit"
          className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
        >
          검색
        </button>
      </form>

      {/* 카테고리 버튼 */}
      <div className="flex flex-wrap gap-2 mb-4">
        {categories.map((cat) => (
          <button
            key={cat.id}
            onClick={() => onChange({ ...filter, categoryId: cat.id })}
            className={`px-3 py-1 rounded border ${
              filter.categoryId === cat.id
                ? 'bg-blue-500 text-white'
                : 'bg-white text-gray-700 hover:bg-gray-100'
            }`}
          >
            {cat.name}
          </button>
        ))}
      </div>

      {/* 검색 범위 체크박스 */}
      <div className="flex gap-4 text-sm text-gray-700">
        <label>
          <input
            type="checkbox"
            checked={filter.searchFields?.includes('title')}
            onChange={() => handleFieldToggle('title')}
          />{' '}
          제목
        </label>
        <label>
          <input
            type="checkbox"
            checked={filter.searchFields?.includes('content')}
            onChange={() => handleFieldToggle('content')}
          />{' '}
          내용
        </label>
        <label>
          <input
            type="checkbox"
            checked={filter.searchFields?.includes('comment')}
            onChange={() => handleFieldToggle('comment')}
          />{' '}
          댓글
        </label>
      </div>
    </div>
  );
}
