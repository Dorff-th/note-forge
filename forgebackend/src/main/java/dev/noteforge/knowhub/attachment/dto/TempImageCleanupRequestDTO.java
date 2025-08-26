package dev.noteforge.knowhub.attachment.dto;

import dev.noteforge.knowhub.attachment.enums.UploadMode;
import lombok.*;

import java.util.List;

/**
 * 에디터(ToastUI 마크디운)에 작성된 내용중 첨부된 이미지를 다시 삭제할경우에 필요한 요청 DTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TempImageCleanupRequestDTO {
    private String tempKey;     // 새로운 post 작성시 에디터에서 첨부하는 이미지 임시 식별키(UUID 형식)
    private Long postId;                   // 기존 글 수정 시 사용됨
    private UploadMode mode;    //  새로운 POST 작성시 TEMP, 기존 POST 수정시 POST
    private List<String> storedNames;   // 실제 서버에 저장되는 파일명 (file_name : UUID형식.png 등 이미지 파일명)
}
