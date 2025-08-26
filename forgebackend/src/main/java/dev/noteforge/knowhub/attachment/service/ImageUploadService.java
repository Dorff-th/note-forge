package dev.noteforge.knowhub.attachment.service;

import dev.noteforge.knowhub.attachment.domain.Attachment;
import dev.noteforge.knowhub.attachment.dto.FileSaveResultDTO;
import dev.noteforge.knowhub.attachment.dto.TempImageCleanupRequestDTO;
import dev.noteforge.knowhub.attachment.enums.UploadMode;
import dev.noteforge.knowhub.attachment.repository.ImageUploadRepository;
import dev.noteforge.knowhub.attachment.util.ImageUploadUtil;
import dev.noteforge.knowhub.post.domain.Post;
import dev.noteforge.knowhub.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageUploadService {

    private final ImageUploadRepository imageUploadRepository;
    private final ImageUploadUtil fileUtil;
    private final PostRepository postRepository;

    //에디터 이미지 첨부파일 저장
    public Attachment uploadEditorImageFile(FileSaveResultDTO dto) {
        Attachment.AttachmentBuilder builder = Attachment.builder()
                .fileName(dto.getFileName())
                .originFileName(dto.getOriginFileName())
                .fileType(dto.getFileType())
                .fileSize(dto.getSize())
                .fileUrl(dto.getFileUrl())
                .publicUrl(dto.getPublicUrl())
                .uploadType(dto.getUploadType())
                .uploadedAt(LocalDateTime.now());

        if (dto.getTempKey() != null) {
            builder.tempKey(dto.getTempKey());  // TEMP 모드
        }

        if (dto.getPostId() != null) {
            Post post = postRepository.findById(dto.getPostId())
                    .orElseThrow(() -> new IllegalArgumentException("Post not found: " + dto.getPostId()));
            builder.post(post);  // POST 모드
        } else {
            builder.post(null);  // 작성 중이거나 tempKey 기반일 때
        }

        return imageUploadRepository.save(builder.build());

    }

    //일반 첨부파일 저장(다중 파일 업로드)
    public List<Attachment> uploadAttachments(Post post,  List<FileSaveResultDTO> fileResults) {

        List<Attachment> attachments = fileResults.stream()
                .map(dto -> Attachment.builder()
                        .post(post)
                        .fileName(dto.getFileName())
                        .originFileName(dto.getOriginFileName())
                        .fileType(dto.getFileType())
                        .fileSize(dto.getSize())
                        .fileUrl(dto.getFileUrl())
                        .uploadType(dto.getUploadType())
                        .uploadedAt(LocalDateTime.now())
                        .build())
                .toList();


        return imageUploadRepository.saveAll(attachments);
    }

    //작성중인 post에 첨부한(삽입한) 이미지를 다시 삭제할때 삭제 대상 file_name으로 찾아서 삭제
    @Transactional
    public void cleanupUnusedTempImages(TempImageCleanupRequestDTO request) {

        if(request.getMode() == UploadMode.CREATE) {

            String tempKey = request.getTempKey();
            List<String> storedNames = request.getStoredNames();

            //삭제 대상 storedNames(즉 fileName들)을 조회하여
            List<Attachment> unusedTempImages = imageUploadRepository.findUnusedImagesByTempKey(tempKey, storedNames);

            //삭제대상 storeName 콘솔로 확인
            log.debug("\n\n\n====삭제대상 storeName 콘솔로 확인");
            log.debug("tempKey : " + tempKey);
            unusedTempImages.forEach(img->{log.debug(img.getFileName());});

            //삭제 대상 이미지 파일(서버에 저장된 실제파일명)을 리스트화
            List<String> unusedFileNames = unusedTempImages.stream()
                    .map(img->img.getFileName())
                    .collect(Collectors.toList());

            if (!unusedFileNames.isEmpty()) {
                imageUploadRepository.deleteByTempKeyAndStoredNamesByTempKey(tempKey, unusedFileNames);
            }

        } else if(request.getMode() == UploadMode.UPDATE) {
            // TODO: 기존 postId 기반 미사용 이미지 정리 로직
            Long postId = request.getPostId();
            List<String> storedNames = request.getStoredNames();
            List<Attachment> unusedTempImages = imageUploadRepository.findUnusedImagesByPostId(postId, storedNames);

            //삭제 대상 storedNames(즉 fileName들)을 조회하여
            //삭제대상 storeName 콘솔로 확인
            log.info("\n\n\n====삭제대상 storeName 콘솔로 확인");
            log.info("postId : " + postId);
            unusedTempImages.forEach(img->{log.debug(img.getFileName());});

            //삭제 대상 이미지 파일(서버에 저장된 실제파일명)을 리스트화
            List<String> unusedFileNames = unusedTempImages.stream()
                    .map(img->img.getFileName())
                    .collect(Collectors.toList());

            if (!unusedFileNames.isEmpty()) {
                imageUploadRepository.deleteByTempKeyAndStoredNamesByPostId(postId, unusedFileNames);
            }

        }

    } // method end
}
