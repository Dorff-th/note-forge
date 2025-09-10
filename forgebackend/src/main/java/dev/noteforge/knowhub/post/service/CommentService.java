package dev.noteforge.knowhub.post.service;

import dev.noteforge.knowhub.comment.domain.Comment;
import dev.noteforge.knowhub.comment.dto.CommentResponseDTO;
import dev.noteforge.knowhub.comment.repository.CommentRepository;
import dev.noteforge.knowhub.member.domain.Member;
import dev.noteforge.knowhub.member.repository.MemberRepository;
import dev.noteforge.knowhub.post.domain.Post;
import dev.noteforge.knowhub.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public List<CommentResponseDTO> getCommentList(Long postId) {
        return commentRepository.findAllByPostId(postId);
    }

    @Transactional
    public CommentResponseDTO createComment(Long postId, Long memberId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        Comment comment = Comment.builder()
                .post(post)
                .member(member)
                .content(content)
                .build();

        Comment saved = commentRepository.save(comment);
        return CommentResponseDTO.fromEntity(saved);
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if (!comment.getPost().getId().equals(postId)) {
            throw new IllegalArgumentException("해당 게시물의 댓글이 아닙니다.");
        }
        if (!comment.getMember().getId().equals(memberId)) {
            throw new SecurityException("본인이 작성한 댓글만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }
}
