import type { SearchFilter } from '@/types/SearchFilter';

interface Category {
  id: number;
  name: string;
}

interface PostSearchFilterProps {
  categories: Category[];
  filter: SearchFilter;
  onChange: (filter: SearchFilter) => void;
  onSearch: () => void; // SearchPage에서 handleSearch 전달
}

export default function PostSearchFilter({
  categories,
  filter,
  onChange,
  onSearch,
}: PostSearchFilterProps) {
  // ✅ 체크박스 토글
  const handleFieldToggle = (field: string) => {
    const exists = filter.searchFields?.includes(field);
    onChange({
      ...filter,
      searchFields: exists
        ? filter.searchFields?.filter((f) => f !== field) || []
        : [...(filter.searchFields || []), field],
    });
  };

  // ✅ 카테고리 클릭
  const handleCategoryClick = (catId: number) => {
    onChange({ ...filter, categoryId: catId });
    onSearch(); // 필터 적용 즉시 검색
  };

  return (
    <div className="mb-6 p-4 border rounded bg-white shadow-sm">
      {/* 카테고리 버튼 */}
      <div className="flex flex-wrap gap-2 mb-4">
        {categories.map((cat) => (
          <button
            key={cat.id}
            type="button"
            onClick={() => handleCategoryClick(cat.id)}
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
