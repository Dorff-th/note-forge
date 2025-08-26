package dev.noteforge.knowhub.attachment.util;

import dev.noteforge.knowhub.attachment.dto.FileSaveResultDTO;
import dev.noteforge.knowhub.attachment.enums.UploadMode;
import dev.noteforge.knowhub.attachment.enums.UploadType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 첨부파일 업로드 공통 클래스(일반첨부, 에디터이미지 첨부)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ImageUploadUtil {

    //application.yml 에 설정한  에디터 이미지 첨부파일. 실제 서버 경로 값을 가져온다.
    @Value("${file.upload.path.images.base-dir}")
    private String imageBaseDir;

    //application.yml 에 설정한  에디터 이미지 첨부파일. 외보 공개 URL 값을 가져온다.
    @Value("${file.upload.path.images.public-url}")
    private String imagePublicUrl;




    @PostConstruct
    public void init() {
            initPath(imageBaseDir);
        }

    private void initPath(String path) {
        File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs(); // 하위 디렉토리까지 생성
            }
        log.info("Editor Image Upload path initialized: {}", dir.getAbsolutePath());
    }

    /** ✅ 단일 파일 저장 (에디터용 이미지) */
    public FileSaveResultDTO saveEditorImageFile(MultipartFile file, UploadMode mode, String identifier) {
        return saveSingleFile(file, imageBaseDir, UploadType.EDITOR_IMAGE, mode, identifier);
    }


    private FileSaveResultDTO saveSingleFile(MultipartFile file, String uploadDir, UploadType uploadType, UploadMode mode, String identifier) {
        try {
            String originalName = file.getOriginalFilename();
            String savedName = UUID.randomUUID() + "_" + originalName;

            File target = new File(uploadDir, savedName);
            file.transferTo(target);

            // public URL 생성 (※ /uploads/images/ 는 설정된 정적 매핑 경로에 따라 조정)
            //String publicUrl = "/uploads/images/" + savedName;   (일반 첨부파일 구현시 수정 필요)
            String imagePublicUrl = "/uploads/images/" + savedName;


            FileSaveResultDTO.FileSaveResultDTOBuilder builder = FileSaveResultDTO.builder()
                    .fileName(savedName)
                    .originFileName(originalName)
                    .fileUrl(uploadDir + savedName)     // 실제 파일이 서버의 물리경로
                    .publicUrl(imagePublicUrl)             // 외부 경로
                    .fileType(file.getContentType())
                    .size(file.getSize())
                    .uploadType(uploadType);

            if (mode == UploadMode.CREATE) {
                builder.tempKey(identifier);
            } else if (mode == UploadMode.UPDATE) {
                try {
                    builder.postId(Long.parseLong(identifier));  // 🔥 여기를 Long으로 변환
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("postId(identifier) 값이 숫자가 아닙니다: " + identifier);
                }
            }

            FileSaveResultDTO resultDTO = builder.build();

            return resultDTO;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + file.getOriginalFilename(), e);
        }
    }

}
