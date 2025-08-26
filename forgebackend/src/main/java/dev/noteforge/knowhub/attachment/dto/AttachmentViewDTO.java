package dev.noteforge.knowhub.attachment.dto;

import dev.noteforge.knowhub.attachment.enums.UploadType;
import lombok.*;

/**
 *  Post id로 조회한 일반 첨부파일의 필요한 정보를 View로 넘겨주기 위한 DTO
 */
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AttachmentViewDTO {

    private Long id;              // 다운로드 URL 생성용
    private String originalName;  // 화면에 보여줄 파일명
    private String fileSizeText;  // 화면용: "2.3 MB"
    private UploadType uploadType;    // 필요 시 구분자

}
