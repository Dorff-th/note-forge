package dev.noteforge.knowhub.attachment.service;

import dev.noteforge.knowhub.attachment.domain.Attachment;
import dev.noteforge.knowhub.attachment.enums.UploadType;
import dev.noteforge.knowhub.attachment.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentService {

    final AttachmentRepository attachmentRepository;

    //post id로 첨부파일 정보 조회
    public List<Attachment> attachmentsByPostId(Long postId, UploadType type) {
        return attachmentRepository.findByPostId(postId, type);

    }

    //Attachment id별로 조회
    public Optional<Attachment> getById(Long id) {
        return attachmentRepository.findById(id);
    }
}
