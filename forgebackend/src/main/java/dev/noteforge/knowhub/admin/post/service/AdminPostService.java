package dev.noteforge.knowhub.admin.post.service;

import dev.noteforge.knowhub.attachment.repository.AttachmentRepository;
import dev.noteforge.knowhub.comment.repository.CommentRepository;
import dev.noteforge.knowhub.post.repository.PostRepository;
import dev.noteforge.knowhub.tag.repository.PostTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 게시물(Post) 관리 Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminPostService {

    private final CommentRepository commentRepository;
    private final PostTagRepository postTagRepository;
    private final AttachmentRepository attachmentRepository;
    private final PostRepository postRepository;

    @Transactional
    public void deletePosts(List<Long> postIds) {
        // 1. 하위 엔티티 먼저 삭제
        commentRepository.deleteByPostIdIn(postIds);
        postTagRepository.deleteByPostIdIn(postIds);
        attachmentRepository.deleteByPostIdIn(postIds);

        // 2. 마지막으로 게시글 삭제
        postRepository.deleteByIdIn(postIds);
    }
}
