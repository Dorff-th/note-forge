// src/types/SearchFilter.ts
export interface SearchFilter {
  keyword?: string; // 검색어
  categoryId?: number; // 카테고리 ID
  writer?: string; // 작성자
  tag?: string; // 태그
  searchFields?: string[]; // ✅ 추가
}
