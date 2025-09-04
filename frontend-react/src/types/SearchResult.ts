// src/types/SearchResult.ts

export interface SearchResult {
  postId: number; // 게시글 ID
  title: string; // 게시글 제목
  content: string; // 게시글 내용 (원문)
  createdAt: string; // 작성일시 (ISO 문자열로 내려올 것)
  writerName: string; // 작성자 이름
  categoryName: string; // 카테고리명

  commentContent?: string; // 댓글 내용 (있을 경우)
  commentWriter?: string; // 댓글 작성자

  // 후처리 필드
  summary?: string; // 키워드 중심 요약
  matchedField?: string; // 어떤 필드에서 매치됐는지
  highlightedTitle?: string; // 키워드 하이라이트된 제목
}
