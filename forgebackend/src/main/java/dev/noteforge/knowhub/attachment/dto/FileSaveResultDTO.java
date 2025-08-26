package dev.noteforge.knowhub.attachment.dto;

import dev.noteforge.knowhub.attachment.enums.UploadType;
import lombok.*;

/**
 * CustomFileUtil에서 저장된 파일 정보를 담는 DTO
 * post_id, uploaded_at은 AttachementService에서 이 DTO를 Attachement 엔티티로 변환할때 주입
 */
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileSaveResultDTO {

    private String fileName;        //  서버에 저장되는 실제 파일명(UUID 형식)
    private String originFileName;  // 업로드 할때 로컬파일 명(원래 파일이름)
    private String fileUrl;         // 서버에 저장되는 실제 파일 경로
    private String fileType;     // 확장자
    private long size;              // 파일 사이즈(byte)
    private UploadType uploadType;      // 에디터 첨부 이미지파일 (EDITOR_IMAGE), 일반 첨부파일(ATTACHMENT)
    private String publicUrl;    // 클라이언트가 접근할 수 있는 경로 (예: /uploads/images/xxx.png)

    // ✅ 게시글 작성 중: tempKey 사용
    private String tempKey;

    // ✅ 게시글 수정 중: postId 사용
    private Long postId;
}
