package dev.noteforge.knowhub.post.dto;

import dev.noteforge.knowhub.attachment.dto.AttachmentViewDTO;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/*
Post 상세내용을 반환하는 DTO
 */


@NoArgsConstructor
//@Builder
@Data
public class PostDetailDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String categoryName;
    private Long memberId;
    private String username;
    private Long categoryId;
    private String nickname;

    private List<AttachmentViewDTO> attachments;



    public PostDetailDTO(Long id, String title, String content, LocalDateTime createdAt,
                         LocalDateTime updatedAt, String categoryName, Long memberId,
                         String username, Long categoryId, String nickname) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.categoryName = categoryName;
        this.memberId = memberId;
        this.username = username;
        this.categoryId = categoryId;
        this.nickname = nickname;

    }

    public void setAttachments(List<AttachmentViewDTO> attachments) {
        this.attachments = attachments;
    }
}
