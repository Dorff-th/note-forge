package dev.noteforge.knowhub.post.dto.projection;

import java.time.LocalDateTime;

/**
 * 게시글 리스트 뷰에 필요한 최소한의 요약 정보를 담는 Projection 인터페이스입니다.
 *
 * 주로 게시판 목록, 관리자 게시물 관리 화면 등에 사용됩니다.
 * 제목, 작성자, 카테고리, 작성일 등만 포함되며, 본문(content) 등의 상세 정보는 포함되지 않습니다.
 */
public interface PostSummaryProjection {
    Long getId();
    String getTitle();
    LocalDateTime getCreatedAt();
    String getCategoryName();
    String getUsername();
    Long getMemberId();
}
