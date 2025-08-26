package dev.noteforge.knowhub.attachment.controller;

import dev.noteforge.knowhub.attachment.domain.Attachment;
import dev.noteforge.knowhub.attachment.dto.FileSaveResultDTO;
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
    public ResponseEntity<?>uploadEditorImage(
            @RequestParam("image") MultipartFile file,
            @RequestParam("mode") String mode,
            @RequestParam("identifier") String identifier) {

        try {

            String originFilename = file.getOriginalFilename();
            System.out.println(originFilename);
            //허용된 확장자가 아니면 프론트에 아래와 같은 정보를 전달하고 메서드 종료처리

            if (!isAllowedExtensions(originFilename)) {
                System.out.println("허용된 확장자가 아닙니다.!");
                return ResponseEntity.badRequest().body(Map.of(
                        "success", 0,
                        "message", "PNG, JPG, JPEG, GIF 형식의 이미지 파일만 업로드 가능합니다."
                ));
            }

            if (file.getSize() > MAX_EDITOR_IMAGE_SIZE) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", 0,
                        "message", "에디터 이미지는 1MB를 초과할 수 없습니다."
                ));
            }

            // 1. 파일 저장 (FileUtil에서)
            UploadMode uploadMode = UploadMode.valueOf(mode);
            FileSaveResultDTO fileDto = imageUploadUtil.saveEditorImageFile(file, uploadMode, identifier);  // DTO 반환

            // 2. 서비스에서 Attachment 엔티티 생성 하고 DB에 저장
            Attachment attachment = imageUploadService.uploadEditorImageFile(fileDto);

            // 3. 에디터가 요구하는 응답 포맷
            return ResponseEntity.ok(Map.of(
                    "success", 1,
                    "url", fileDto.getPublicUrl()
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "success", 0,
                    "message", "이미지 업로드 중 오류가 발생했습니다."
            ));
        }

    } // method end

    @PostMapping("/api/images/temp/cleanup")
    public ResponseEntity<?> cleanupTempImages(@RequestBody TempImageCleanupRequestDTO request) {

        //String tempKey = request.getTempKey();
        //List<String> storedNames = request.getStoredNames();

        // 서비스 호출
        imageUploadService.cleanupUnusedTempImages(request);

        return ResponseEntity.ok().build();
    }

}
