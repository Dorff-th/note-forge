package dev.noteforge.knowhub.post.service;

import dev.noteforge.knowhub.comment.dto.CommentResponseDTO;
import dev.noteforge.knowhub.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;

    public List<CommentResponseDTO> getCommentList(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }
}
