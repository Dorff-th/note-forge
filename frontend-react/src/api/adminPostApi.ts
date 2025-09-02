import axiosInstance from '@/api/axiosInstance';
import type { PostDTO, PostDetailDTO } from '@/types/Post';
import type { PageResponse } from '@/types/Common';
import type { CommentResponse } from '@/types/Comment';

// 게시글 목록 조회
export const fetchAdminPosts = async (
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

// 게시글 상세 조회
export const fetchAdminPostDetail = async (id: number): Promise<PostDetailDTO> => {
  const response = await axiosInstance.get<PostDetailDTO>(`/admin/posts/${id}`);
  return response.data;
};

// 게시글 단일 삭제
export const deleteAdminPost = async (id: number): Promise<void> => {
  await axiosInstance.delete(`/admin/posts/${id}`);
};

// 여러 게시글 일괄 삭제
export const deleteAdminPosts = async (ids: number[]): Promise<void> => {
  await axiosInstance.delete('/admin/posts', { data: ids });
};

// 특정 게시글의 댓글 목록 조회
export async function fetchCommentsByPostId(postId: number): Promise<CommentResponse[]> {
  const res = await axiosInstance.get<CommentResponse[]>(`/admin/comments/post/${postId}`);
  return res.data;
}

// 댓글 일괄 삭제
export async function deleteComments(commentIds: number[]): Promise<void> {
  await axiosInstance.delete('/admin/comments', { data: commentIds });
}
