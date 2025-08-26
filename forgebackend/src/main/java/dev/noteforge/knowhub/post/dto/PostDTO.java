package dev.noteforge.knowhub.post.dto;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/*
Post 목록을 반환하는 DTO
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class PostDTO {
    private Long id;
    private String categoryName;
    private String title;
    private Long memberId;
    private String username;
    private LocalDateTime createdAt;
    private Long commentCount;
    private String nickname;
    private Long attachmentCount;

    public PostDTO(Long id, String title, LocalDateTime createdAt, String categoryName, String username,  Long memberId, Long commentCount, String nickname, Long attachmentCount) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.categoryName = categoryName;
        this.username = username;
        this.memberId = memberId;
        this.commentCount = commentCount;
        this.nickname = nickname;
        this.attachmentCount = attachmentCount;
    }

    // Object[]로 받은 네이티브 쿼리 결과를 DTO로 변환하는 메서드
    public static PostDTO objectToDTO(Object[] objects) {

        return PostDTO.builder()
                .id((Long) objects[0])
                .categoryName((String) objects[1])
                .title((String) objects[2])
                .memberId((Long) objects[3])
                .username((String) objects[4])
                .createdAt(((Timestamp) objects[5]).toLocalDateTime())
                .build();

    }
}
