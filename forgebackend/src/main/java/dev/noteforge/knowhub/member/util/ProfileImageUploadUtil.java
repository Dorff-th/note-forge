package dev.noteforge.knowhub.member.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 *  사용자 프로파일 이미지 업로드 유틸
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileImageUploadUtil {

    @Value("${file.upload.path.profile-image.base-dir}")
    private String profileImageBaseDir;

    @Value("${file.upload.path.profile-image.public-url}")
    private String profileImagePublicUrl;

    @PostConstruct
    private void init() {
        File dir = new File(profileImageBaseDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        log.info("User Profile Image Upload path initialized: {}", dir.getAbsolutePath());
    }

    /**
     * 프로필 이미지 저장 (300x300 PNG 리사이즈)
     */
    public String saveProfileImage(Long memberId, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("업로드된 파일이 비어있습니다.");
        }

        // ✅ 용량 제한 (예: 1MB)
        long maxSize = 1 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("파일 용량이 너무 큽니다. (최대 1MB)");
        }

        // ✅ 이미지 여부 검사
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }

        // ✅ 저장 파일명 (항상 PNG)
        String fileName = "member_" + memberId + ".png";
        File dest = new File(profileImageBaseDir, fileName);

        // ✅ 기존 파일 삭제
        if (dest.exists()) {
            dest.delete();
        }

        // ✅ 리사이즈 & PNG 변환
        Thumbnails.of(file.getInputStream())
                .size(300, 300)        // 리사이즈
                .outputFormat("png")   // PNG 변환
                .toFile(dest);

        log.info("프로필 이미지 저장 완료 (300x300 PNG): {}", dest.getAbsolutePath());
        return profileImagePublicUrl + fileName;
    }

    /**
     * 프로필 이미지 삭제
     */
    public void deleteProfileImage(Long memberId) {
        File file = new File(profileImageBaseDir, "member_" + memberId + ".png");
        if (file.exists() && file.delete()) {
            log.info("프로필 이미지 삭제 완료: {}", file.getAbsolutePath());
        }
    }

    /**
     * 프로필 이미지 URL 반환 (없으면 기본 이미지)
     */
    public String getProfileImageUrl(Long memberId) {
        File file = new File(profileImageBaseDir, "member_" + memberId + ".png");
        if (file.exists()) {
            return profileImagePublicUrl + file.getName();
        }
        return "/images/default-profile.png"; // 정적 리소스 경로
    }
}

