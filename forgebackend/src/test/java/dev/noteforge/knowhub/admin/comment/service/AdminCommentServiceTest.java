package dev.noteforge.knowhub.admin.comment.service;

import dev.noteforge.knowhub.comment.dto.CommentResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminCommentServiceTest {

    @Autowired
    private AdminCommentService adminCommentService;

    @DisplayName("코멘트 확인")
    @Test
    void testGetComment() {
        Long postId = 79L;

        List<CommentResponseDTO> comments = adminCommentService.getCommentsByPostId(postId);
        comments.forEach(comment -> {System.out.println(comment);});

    }

}