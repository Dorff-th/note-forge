package dev.noteforge.knowhub.attachment.controller;

import dev.noteforge.knowhub.attachment.domain.Attachment;
import dev.noteforge.knowhub.attachment.service.AttachmentService;
import dev.noteforge.knowhub.attachment.util.GeneralFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;
    private final GeneralFileUtil fileUtil;

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable("id") Long id) {
        Attachment attachment = attachmentService.getById(id).orElseThrow(() -> new IllegalArgumentException("해당 id를 갖는 첨부파일이 없습니다!"));
        return fileUtil.getDownloadResponse(attachment.getFileUrl(), attachment.getOriginFileName());
    }
}
