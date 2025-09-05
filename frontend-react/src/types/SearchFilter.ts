// src/types/SearchFilter.ts
export interface SearchFilter {
  keyword: string;
  categoryId?: number | 'all';
  searchFields: string[]; // ['title', 'content', 'comment']
  dateFrom?: string; // yyyy-MM-dd
  dateTo?: string; // yyyy-MM-dd
}
