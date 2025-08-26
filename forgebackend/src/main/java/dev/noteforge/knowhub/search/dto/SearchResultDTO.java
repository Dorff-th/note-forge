package dev.noteforge.knowhub.search.dto;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class SearchResultDTO {
    private Long postId;         // 게시글 ID (p.id)
    private String title;        // 게시글 제목 (p.title)
    private String content;
    private LocalDateTime createdAt;    // 작성일시 (p.created_at → String 가공)
    private String writerName;   // 게시글 작성자 (m.username)
    private String categoryName; // 카테고리명 (c.name)

    private String commentContent;  // 댓글 내용 (cm.content)
    private String commentWriter;   // 댓글 작성자 (cm_writer.username)

    // ↓ Java 후처리로 세팅할 필드
    private String summary;         // 키워드 중심 요약 (본문, 댓글 등에서 추출)
    private String matchedField;    // 어떤 필드에서 키워드가 매치됐는지

    private String highlightedTitle; //키워드 형광색 표시를 위한 필드
}
