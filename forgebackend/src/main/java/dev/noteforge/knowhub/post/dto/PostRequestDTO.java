package dev.noteforge.knowhub.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 신규 Post 등록 요청에 사용되는 DTO
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PostRequestDTO {

    @NotBlank(message = "{NotBlankPostTitle}")
    private String title;

    @NotBlank(message = "{NotBlankPostContent}")
    private String content;

    private Long memberId;

    private Long categoryId;

    private String tempKey;

    //첨부파일
    List<MultipartFile> attachments;

    //Tags
    private String tags; // hidden input "tags"
}
