// src/api/postApi.ts
import axiosInstance from './axiosInstance';
import type { PostDTO, PostDetailDTO } from '@/types/Post';
import type { PageResponse } from '@/types/Common';
import type { CommentResponse } from '@/types/Comment';

export const fetchPosts = async (
  page: number,
  size: number = 10,
  sort: string = 'createdAt',
  direction: 'ASC' | 'DESC' = 'DESC',
): Promise<PageResponse<PostDTO>> => {
  const response = await axiosInstance.get<PageResponse<PostDTO>>('/posts', {
    params: { page, size, sort, direction },
  });
  return response.data;
};

// 게시글 상세 조회
export const fetchPostDetail = async (id: number): Promise<PostDetailDTO> => {
  const response = await axiosInstance.get<PostDetailDTO>(`/posts/${id}`);
  return response.data;
};

// 특정 게시글의 댓글 목록 조회
export async function fetchCommentsByPostId(postId: number): Promise<CommentResponse[]> {
  const res = await axiosInstance.get<CommentResponse[]>(`/posts/${postId}/comments`);
  return res.data;
}
