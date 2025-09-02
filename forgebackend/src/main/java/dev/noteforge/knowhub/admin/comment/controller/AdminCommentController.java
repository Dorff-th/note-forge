package dev.noteforge.knowhub.admin.comment.controller;

import dev.noteforge.knowhub.admin.comment.service.AdminCommentService;
import dev.noteforge.knowhub.comment.dto.CommentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {

    private final AdminCommentService adminCommentService;

    /**
     * 특정 게시글의 댓글 목록 조회
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByPostId(@PathVariable("postId") Long postId) {
        List<CommentResponseDTO> comments = adminCommentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    /**
     * 선택된 댓글 일괄 삭제
     */
    @DeleteMapping
    public ResponseEntity<String> deleteComments(@RequestBody List<Long> commentIds) {
        adminCommentService.deleteComments(commentIds);
        return ResponseEntity.ok("Deleted " + commentIds.size() + " comments");
    }
}
