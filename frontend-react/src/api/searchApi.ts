// src/api/searchApi.ts
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

// 통합검색 호출
export async function searchPosts(
  params: SearchFilter & { page?: number; size?: number },
): Promise<SearchApiResponse> {
  const res = await axiosInstance.get('/search', { params });
  return res.data;
}
