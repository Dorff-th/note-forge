package dev.noteforge.knowhub.attachment.controller;


import dev.noteforge.knowhub.attachment.dto.TempImageCleanupRequestDTO;
import dev.noteforge.knowhub.attachment.enums.UploadMode;
import dev.noteforge.knowhub.attachment.service.ImageUploadService;
import dev.noteforge.knowhub.attachment.util.ImageUploadUtil;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 에디터 이미지 첨부 업로드 Api Controller
 */

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageUploadApiController {

    private final ImageUploadService imageUploadService;
    private final ImageUploadUtil imageUploadUtil;

    //에디터에서 이미지 파일 등록시 특정 확장자만 허용
    private static final List<String> ALLOWED_EXTENSIONS = List.of("png", "jpg", "jpeg", "gif");

    private boolean isAllowedExtensions(String filename) {
        String ext = StringUtils.getFilenameExtension(filename);
        return ext != null && ALLOWED_EXTENSIONS.contains(ext.toLowerCase());
    }

    //이미지 용량만 별도로 제한
    private static final long MAX_EDITOR_IMAGE_SIZE = 1 * 1024 * 1024; // 2MB


    @PostMapping("/api/images/upload")
    public ResponseEntity<?> uploadEditorImage(@RequestParam("image") MultipartFile file) {
        try {
            String fileUrl = imageUploadUtil.uploadImage(file); // ✅ 물리 저장 + URL 반환만

            return ResponseEntity.ok(Map.of(
                    "success", 1,
                    "url", fileUrl
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", 0,
                    "message", "이미지 업로드 중 오류 발생"
            ));
        }
    }


    @PostMapping("/api/images/temp/cleanup")
    public ResponseEntity<?> cleanupTempImages(@RequestBody TempImageCleanupRequestDTO request) {

        //String tempKey = request.getTempKey();
        //List<String> storedNames = request.getStoredNames();

        // 서비스 호출
        imageUploadService.cleanupUnusedTempImages(request);

        return ResponseEntity.ok().build();
    }

}
