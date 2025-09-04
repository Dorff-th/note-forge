// src/types/Search.ts
export interface SearchResult {
  id: number;
  title: string;
  contentSnippet: string; // 검색결과 요약/하이라이트
  writer: string;
  categoryName: string;
  createdAt: string;
  updatedAt: string;
  viewCount: number;
  tags: string[];
}
