// src/api/postApi.ts
import axiosInstance from './axiosInstance';
import type { PostDTO } from '@/types/Post';
import type { PageResponse } from '@/types/Common';

export const fetchPosts = async (
  page: number,
  size: number = 10,
  sort: string = 'createdAt',
  direction: 'ASC' | 'DESC' = 'DESC',
): Promise<PageResponse<PostDTO>> => {
  const response = await axiosInstance.get<PageResponse<PostDTO>>('/admin/posts', {
    params: { page, size, sort, direction },
  });
  return response.data;
};
