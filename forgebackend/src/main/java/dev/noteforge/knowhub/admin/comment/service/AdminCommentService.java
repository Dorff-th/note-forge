package dev.noteforge.knowhub.admin.comment.service;

import dev.noteforge.knowhub.comment.dto.CommentResponseDTO;

import dev.noteforge.knowhub.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminCommentService {

    private final CommentRepository commentRepository;

    /**
     * 특정 게시글의 댓글 목록 조회
     */
    public List<CommentResponseDTO> getCommentsByPostId(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }

    /**
     * 선택된 댓글 일괄 삭제
     */
    public void deleteComments(List<Long> commentIds) {
        commentRepository.deleteAllByIdInBatch(commentIds);
    }
}
