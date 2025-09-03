package dev.noteforge.knowhub.comment.controller;

import dev.noteforge.knowhub.comment.dto.CommentResponseDTO;
import dev.noteforge.knowhub.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByPostId(@PathVariable("postId") Long postId) {
        List<CommentResponseDTO> comments = commentService.getCommentList(postId);
        return ResponseEntity.ok(comments);
    }

}
