//카테고리 목록 조회
import axiosInstance from '@/api/axiosInstance';
import type { Category } from '@/types/Category';

export async function fetchCategories(): Promise<Category[]> {
  const res = await axiosInstance.get('/categories');
  return res.data;
}
