package dev.noteforge.knowhub.comment.controller;

import dev.noteforge.knowhub.comment.dto.CommentRequest;
import dev.noteforge.knowhub.comment.dto.CommentResponseDTO;
import dev.noteforge.knowhub.member.security.MemberDetails;
import dev.noteforge.knowhub.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByPostId(@PathVariable("postId") Long postId) {
        List<CommentResponseDTO> comments = commentService.getCommentList(postId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 작성
    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(
            @PathVariable("postId") Long postId,
            @AuthenticationPrincipal MemberDetails loginUser,
            @RequestBody CommentRequest request
    ) {
        CommentResponseDTO response = commentService.createComment(postId, loginUser.getId(), request.getContent());
        return ResponseEntity.ok(response);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            @AuthenticationPrincipal MemberDetails loginUser
    ) {
        commentService.deleteComment(postId, commentId, loginUser.getId());
        return ResponseEntity.noContent().build();
    }

}
