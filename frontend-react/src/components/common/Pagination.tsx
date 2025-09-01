import React from 'react';

interface PaginationProps {
  page: number;
  startPage: number;
  endPage: number;
  prev: boolean;
  next: boolean;
  onPageChange: (page: number) => void;
}

const Pagination: React.FC<PaginationProps> = ({
  page,
  startPage,
  endPage,
  prev,
  next,
  onPageChange,
}) => {
  return (
    <div className="flex justify-center mt-6 space-x-2">
      {/* 이전 버튼 */}
      {prev && (
        <button
          onClick={() => onPageChange(startPage - 1)}
          className="px-3 py-1 rounded bg-gray-200 hover:bg-gray-300"
        >
          이전
        </button>
      )}

      {/* 페이지 번호 버튼 */}
      {Array.from({ length: endPage - startPage + 1 }, (_, i) => startPage + i).map((pageNum) => (
        <button
          key={pageNum}
          onClick={() => onPageChange(pageNum)}
          className={`px-3 py-1 rounded ${
            page === pageNum ? 'bg-blue-500 text-white' : 'bg-gray-200 hover:bg-gray-300'
          }`}
        >
          {pageNum}
        </button>
      ))}

      {/* 다음 버튼 */}
      {next && (
        <button
          onClick={() => onPageChange(endPage + 1)}
          className="px-3 py-1 rounded bg-gray-200 hover:bg-gray-300"
        >
          다음
        </button>
      )}
    </div>
  );
};

export default Pagination;
