export interface PageResponse<T> {
  page: number;
  size: number;
  totalPages: number;
  totalElements: number;
  startPage: number;
  endPage: number;
  prev: boolean;
  next: boolean;
  dtoList: T[];
  currentPage: number;
}
