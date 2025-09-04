// src/pages/SearchPage.tsx
import { useEffect, useState } from 'react';
import { useSearchParams } from 'react-router-dom';
import { searchPosts } from '@/api/searchApi';
import type { SearchApiResponse } from '@/api/searchApi';
import type { SearchFilter } from '@/types/SearchFilter';
import Pagination from '@/components/common/Pagination';

export default function SearchPage() {
  const [searchParams] = useSearchParams();
  const initialKeyword = searchParams.get('keyword') || '';
  const [filter, setFilter] = useState<SearchFilter>({
    keyword: initialKeyword,
  });

  const [data, setData] = useState<SearchApiResponse | null>(null);
  const [page, setPage] = useState(1);

  //   const [filter, setFilter] = useState<SearchFilter>({
  //     keyword: '',
  //     categoryId: undefined,
  //     writer: '',
  //     tag: '',
  //   });

  useEffect(() => {
    loadData();
  }, [page]);

  const loadData = async () => {
    const res = await searchPosts({ ...filter, page, size: 10 });
    console.log('검색결과', res);
    setData(res);
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setPage(1);
    loadData();
  };

  return (
    <div className="max-w-5xl mx-auto p-6">
      {/* 검색폼 */}
      <form onSubmit={handleSubmit} className="flex gap-2 mb-6">
        <input
          type="text"
          placeholder="검색어를 입력하세요"
          value={filter.keyword}
          onChange={(e) => setFilter({ ...filter, keyword: e.target.value })}
          className="flex-1 border rounded px-3 py-2"
        />
        <button
          type="submit"
          className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
        >
          검색
        </button>
      </form>

      {/* 카테고리 필터 */}
      <div className="flex flex-wrap gap-2 mb-6">
        {data?.categories.map((cat) => (
          <button
            key={cat.id}
            onClick={() => setFilter((prev) => ({ ...prev, categoryId: cat.id }))}
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

      {/* 검색 결과 */}
      <div className="space-y-4">
        {data?.result?.dtoList?.length === 0 && (
          <p className="text-gray-500">검색 결과가 없습니다.</p>
        )}

        {data?.result?.dtoList?.map((item) => (
          <div key={item.postId} className="p-4 border rounded">
            <h3>{item.title}</h3>
            <p className="text-gray-600 text-sm">{new Date(item.createdAt).toLocaleDateString()}</p>
            <p className="line-clamp-2 text-gray-700">{item.content}</p>
          </div>
        ))}
      </div>

      {/* 페이징 */}
      {data && (
        <Pagination
          page={page}
          startPage={data.result.startPage}
          endPage={data.result.endPage}
          prev={data.result.prev}
          next={data.result.next}
          onPageChange={setPage}
        />
      )}
    </div>
  );
}
