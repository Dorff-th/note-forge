// types/post.ts
// 게시글 목록용 DTO
export interface PostDTO {
  id: number;
  categoryName: string;
  title: string;

  memberId: number;
  nickname: string; // ✅ username 제외

  createdAt: string;

  commentCount: number;
  attachmentCount: number;
}

// 게시글 상세보기용 DTO
export interface PostDetailDTO {
  id: number;
  title: string;
  content: string;

  createdAt: string;
  updatedAt: string;

  categoryName: string;
  categoryId: number;

  memberId: number;
  username: string;
  nickname: string;
}
