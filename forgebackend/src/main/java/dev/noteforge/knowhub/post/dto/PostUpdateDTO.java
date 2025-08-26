package dev.noteforge.knowhub.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 *   기존 Post 수정 요청을 하는 DTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostUpdateDTO {

    private Long id;
    @NotBlank(message = "{NotBlankPostTitle}")
    private String title;

    @NotBlank(message = "{NotBlankPostContent}")
    private String content;

    private Long memberId;

    private Long categoryId;

    private LocalDateTime updatedAt;

    public LocalDateTime getUpdatedAt() {
        return LocalDateTime.now();
    }

    // 신규로 들어가는 첨부파일
    List<MultipartFile> attachments;

    //기존 첨부파일 삭제대상 attachment의 id
    List<Long> deleteIds;

    private String tags; // hidden input "tags"  (신규 입력 tag 들)
    
    private String deleteTagIds;  // 삭제 대상 tag id 들

}
