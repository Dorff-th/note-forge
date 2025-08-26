package dev.noteforge.knowhub.member.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 *  사용자 프로파일 이미지 업로드 유틸
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileImageUploadUtil {

    //application.yml 에 설정한  에디터 이미지 첨부파일. 실제 서버 경로 값을 가져온다.
    @Value("${file.upload.path.profile-image.base-dir}")
    private String profileImageBaseDir;

    //application.yml 에 설정한  에디터 이미지 첨부파일. 외보 공개 URL 값을 가져온다.
    @Value("${file.upload.path.profile-image.public-url}")
    private String profileImagePublicUrl;

    //@PostConstruct
    private void init(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs(); // 하위 디렉토리까지 생성
        }
        log.info("User Profile Image Upload path initialized: {}", dir.getAbsolutePath());
    }
}
