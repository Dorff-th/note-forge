package dev.noteforge.knowhub.attachment.domain;

import dev.noteforge.knowhub.attachment.enums.UploadType;
import dev.noteforge.knowhub.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "attachment")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column(nullable = false)
    //private Long postId;

    private String fileName;    // 서버에 저장되는 실제 파일이름(파일이름은 UUID 형식)
    private String fileUrl;  // 실제 서버에 저장되는 물리경로 (예 C:/xxx/xxx)

    private String publicUrl;    // 웹브라우저 url 호출경로 (예 localhost:8080/upload/images/)

    private String originFileName;  // 첨부파일 원래 이름

    private String fileType;

    private Long fileSize;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    @Enumerated(EnumType.STRING) // ← 반드시 추가!
    @Column(name = "upload_type")
    private UploadType uploadType;  // 첨부파일형식 : 에디터 이미지 첨부파일(EDITOR_IMAGE), 일반 첨부파일(ATTACHMENT)

    // 게시글 작성 중 임시로 첨부된 파일을 식별하기 위한 키
    // post_id가 정해지기 전, 첨부파일을 임시로 그룹핑하는 식별자
    // 이미지 업로드 시점과 글 저장 시점 사이의 첨부파일 연결용 임시 키
    // 글 작성 중 첨부된 파일들을 나중에 post에 연결하기 위한 UUID
    // 글 저장 전 업로드된 이미지 식별용 UUID (작성 완료 시 post_id로 매핑됨)
    @Column(name = "temp_key", length = 100)
    private String tempKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    //테스트용 생성자
    public Attachment(Post post, String fileName) {
        this.post = post;
        this.fileName = fileName;
    }
}
