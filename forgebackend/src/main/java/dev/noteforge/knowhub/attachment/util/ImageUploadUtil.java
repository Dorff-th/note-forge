package dev.noteforge.knowhub.attachment.util;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 단일 이미지 업로드 유틸
 * - 글쓰기 최종 저장 시점에만 호출
 * - 임시 tempKey, mode 같은 불필요한 개념 제거
 */
@Component
@Slf4j
public class ImageUploadUtil {

    @Value("${file.upload.path.images.base-dir}")
    private String uploadDir;

    @Value("${file.upload.path.images.public-url}")
    private String uploadUrlPrefix;

    /**
     * 이미지 저장 후 접근 가능한 URL 반환
     */
    public String uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("빈 파일은 업로드할 수 없습니다.");
        }

        // 고유 파일명 생성
        String originalFilename = file.getOriginalFilename();
        String ext = (originalFilename != null && originalFilename.contains("."))
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String filename = UUID.randomUUID().toString() + ext;

        // 저장 경로
        Path savePath = Paths.get(uploadDir).resolve(filename);
        Files.createDirectories(savePath.getParent());

        // 원본 저장
        file.transferTo(savePath);

        // 필요하다면 썸네일 생성 (예시)
        if (isImage(ext)) {
            File thumbnailFile = new File(savePath.getParent().toFile(), "thumb_" + filename);
            Thumbnails.of(savePath.toFile())
                    .size(300, 300)
                    .toFile(thumbnailFile);
        }

        String fileUrl = uploadUrlPrefix + filename;
        log.info("이미지 업로드 완료: {}", fileUrl);

        return fileUrl;
    }

    private boolean isImage(String ext) {
        return ext.equalsIgnoreCase(".jpg") || ext.equalsIgnoreCase(".jpeg")
                || ext.equalsIgnoreCase(".png") || ext.equalsIgnoreCase(".gif")
                || ext.equalsIgnoreCase(".webp");
    }
}
