import { useEffect, useState } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { searchPosts } from '@/api/searchApi';
import type { SearchApiResponse } from '@/api/searchApi';
import type { SearchFilter } from '@/types/SearchFilter';
import Pagination from '@/components/common/Pagination';
import PostSearchFilter from '@/components/search/PostSearchFilter';

export default function SearchPage() {
  const navigate = useNavigate();

  // ✅ URL query에서 keyword 꺼내오기
  const [searchParams] = useSearchParams();
  const initialKeyword = searchParams.get('keyword') || '';

  // ✅ 검색 필터 상태 (전부 POST body로 전달됨)
  const [filter, setFilter] = useState<SearchFilter>({
    keyword: initialKeyword,
    searchFields: [],
  });

  const [data, setData] = useState<SearchApiResponse | null>(null);
  const [page, setPage] = useState(1);

  // ✅ page 또는 filter가 바뀌면 API 호출
  useEffect(() => {
    loadData();
  }, [page, filter]);

  const loadData = async () => {
    const res = await searchPosts(filter, page, 10); // ✅ 변경
    console.log('검색결과', res);
    setData(res);
  };

  return (
    <div className="max-w-5xl mx-auto p-6">
      {/* 🔍 검색 필터 박스 */}
      <PostSearchFilter
        categories={data?.categories || []}
        filter={filter}
        onChange={setFilter}
        onSearch={() => {
          setPage(1);
          loadData();
        }}
      />

      {/* 검색 결과 개수 */}
      {data && (
        <div className="mb-4 text-gray-600">
          총 <span className="font-bold">{data.result.totalElements}</span>건의 검색 결과가
          있습니다.
        </div>
      )}

      {/* 검색 결과 */}
      <div className="space-y-4">
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
            onClick={() => navigate(`/posts/${item.postId}`)}
          >
            <h3
              className="font-bold text-lg mb-2 text-gray-800"
              dangerouslySetInnerHTML={{
                __html: item.highlightedTitle || item.title,
              }}
            />
            <hr className="my-3" />
            <div className="flex flex-wrap items-center text-sm text-gray-500 gap-4">
              <span className="flex items-center gap-1">📂 {item.categoryName}</span>
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
