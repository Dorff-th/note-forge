import axiosInstance from '@/api/axiosInstance';
import type { PageResponse } from '@/types/Common';
import type { SearchResult } from '@/types/SearchResult';
import type { Category } from '@/types/Category';
import type { SearchFilter } from '@/types/SearchFilter';

export interface SearchApiResponse {
  result: PageResponse<SearchResult>;
  categories: Category[];
  searchFilter: SearchFilter;
  queryString: string;
  currentPage: number;
}

// ✅ 백엔드에서 요구하는 JSON 구조에 맞게 수정
export async function searchPosts(
  filter: SearchFilter,
  page: number,
  size: number,
): Promise<SearchApiResponse> {
  const payload = {
    searchFilterDTO: filter,
    pageRequestDTO: {
      page,
      size,
    },
  };

  const res = await axiosInstance.post('/search', payload);
  return res.data;
}
