import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSelector } from 'react-redux';
import type { RootState } from '@/store';
import { searchPosts } from '@/api/searchApi';
import type { SearchApiResponse } from '@/api/searchApi';
import Pagination from '@/components/common/Pagination';

export default function SearchPage() {
  const navigate = useNavigate();
  const searchCondition = useSelector((state: RootState) => state.search);

  const [data, setData] = useState<SearchApiResponse | null>(null);
  const [page, setPage] = useState(1);

  // ✅ 조건/페이지 변경 시 검색 실행
  useEffect(() => {
    if (searchCondition.keyword) {
      loadData(searchCondition, page);
    }
  }, [searchCondition, page]);

  const loadData = async (condition: typeof searchCondition, pageNum: number) => {
    try {
      const res = await searchPosts(condition, pageNum, 10);
      setData(res);
    } catch (err) {
      console.error('검색 실패:', err);
      setData(null);
    }
  };

  return (
    <div className="max-w-5xl mx-auto p-6">
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
              <span>📂 {item.categoryName}</span>
              <span>✍️ {item.writerName}</span>
              <span>🗓 {new Date(item.createdAt).toLocaleDateString()}</span>
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
