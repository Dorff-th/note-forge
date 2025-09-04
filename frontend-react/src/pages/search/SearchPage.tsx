// src/pages/SearchPage.tsx
import { useEffect, useState } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { searchPosts } from '@/api/searchApi';
import type { SearchApiResponse } from '@/api/searchApi';
import type { SearchFilter } from '@/types/SearchFilter';
import Pagination from '@/components/common/Pagination';

export default function SearchPage() {
  const navigate = useNavigate();

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

      {/* 검색 결과 개수 */}
      {data && (
        <div className="mb-4 text-gray-600">
          총 <span className="font-bold">{data.result.totalElements}</span>건의 검색 결과가
          있습니다.
        </div>
      )}

      {/* 검색 결과 */}
      <div className="space-y-4">
        {/* 검색 결과 없음 */}
        {data?.result?.dtoList?.length === 0 && (
          <div className="p-6 text-center text-gray-500 border rounded bg-gray-50">
            검색 결과가 없습니다.
          </div>
        )}

        {data?.result?.dtoList?.map((item) => (
          <div
            key={item.postId}
            className="p-5 bg-white border rounded-lg shadow-sm 
                 hover:shadow-lg hover:-translate-y-1 hover:scale-[1.01]
                 transform transition duration-300 cursor-pointer"
            onClick={() => navigate(`/posts/${item.postId}`)} // ✅ 상세로 이동
          >
            {/* 제목 (하이라이트 적용) */}
            <h3
              className="font-bold text-lg mb-2 text-gray-800"
              dangerouslySetInnerHTML={{
                __html: item.highlightedTitle || item.title,
              }}
            />

            {/* 구분선 */}
            <hr className="my-3" />

            {/* 메타정보 */}
            <div className="flex flex-wrap items-center text-sm text-gray-500 gap-4">
              <span className="flex items-center gap-1">📂 {item.categoryName}</span>
              <span className="flex items-center gap-1">📂 {item.title}</span>
              <span className="flex items-center gap-1">✍️ {item.writerName}</span>
              <span className="flex items-center gap-1">
                🗓 {new Date(item.createdAt).toLocaleDateString()}
              </span>
            </div>
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
