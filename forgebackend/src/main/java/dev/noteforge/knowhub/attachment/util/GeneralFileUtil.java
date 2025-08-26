package dev.noteforge.knowhub.attachment.util;

import dev.noteforge.knowhub.attachment.dto.FileSaveResultDTO;
import dev.noteforge.knowhub.attachment.enums.UploadType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class GeneralFileUtil {

    //application.yml 에 설정한  일반 첨부파일 uploadpath값을 가져온다
    @Value("${file.upload.path.attachments}")
    private String attachmentsPath;

    @PostConstruct
    public void init() {
        initPath(attachmentsPath);
    }

    private void initPath(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs(); // 하위 디렉토리까지 생성
        }
        log.info("Attachment Upload path initialized: {}", dir.getAbsolutePath());
    }

    /** ✅ 다중 파일 저장 (일반 첨부파일) */
    public List<FileSaveResultDTO> saveFiles(List<MultipartFile> files, Long postId) {
        List<FileSaveResultDTO> results = new ArrayList<>();
        for (MultipartFile file : files) {
            results.add(saveSingleFile(file, attachmentsPath, UploadType.ATTACHMENT, postId));
        }
        return results;
    }

    private FileSaveResultDTO saveSingleFile(MultipartFile file, String uploadDir, UploadType uploadType, Long postId) {
        try {
            String originalName = file.getOriginalFilename();
            String savedName = UUID.randomUUID() + "_" + originalName;

            File target = new File(uploadDir, savedName);
            file.transferTo(target);

            FileSaveResultDTO.FileSaveResultDTOBuilder builder = FileSaveResultDTO.builder()
                    .fileName(savedName)
                    .originFileName(originalName)
                    .fileUrl(uploadDir + savedName)     // 실제 파일이 서버의 물리경로
                    .publicUrl(null)             // 외부 경로
                    .fileType(file.getContentType())
                    .size(file.getSize())
                    .uploadType(uploadType)
                    .tempKey(null)          // 추후 작성중인 게시물 임시저장 기능이 필요할때 고려
                    .postId(postId);

            FileSaveResultDTO resultDTO = builder.build();

            return resultDTO;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + file.getOriginalFilename(), e);
        }
    }

    /**
     * 지정된 경로의 파일을 다운로드 가능한 형태로 반환
     *
     * @param fullPath       서버 내부 저장 경로 (ex: /uploads/attachments/UUID_filename.txt)
     * @param originalName   사용자가 업로드한 원본 파일명
     * @return ResponseEntity<Resource> → 파일 다운로드 응답
     */
    public ResponseEntity<Resource> getDownloadResponse(String fullPath, String originalName) {
        try {
            File file = new File(fullPath);
            if (!file.exists()) {
                throw new RuntimeException("파일이 존재하지 않습니다: " + fullPath);
            }

            FileSystemResource resource = new FileSystemResource(file);

            String encodedFileName = URLEncoder.encode(originalName, StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + encodedFileName + "\"")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.contentLength()))
                    .body(resource);

        } catch (Exception e) {
            log.error("파일 다운로드 실패: {}", e.getMessage(), e);
            throw new RuntimeException("파일 다운로드 중 오류가 발생했습니다.");
        }
    }

}
