package dev.noteforge.knowhub.comment.repository;

import dev.noteforge.knowhub.comment.domain.Comment;
import dev.noteforge.knowhub.comment.dto.CommentResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository  extends JpaRepository<Comment, Long> {

    @Query("""
        SELECT 
        new dev.noteforge.knowhub.comment.dto.CommentResponseDTO(
            c.id,
            c.content, 
            c.createdAt,
            c.post.Id,
            m.id,
            m.username,
            m.nickname,
            m.profileImageUrl
           ) 
        FROM Comment c
        INNER JOIN  c.member m
        WHERE c.post.Id = :postId
        """)
    List<CommentResponseDTO> findAllByPostId(@Param("postId") Long postId);

    //코멘트 일괄삭제 (Post가 삭제될때 하위 코멘트 모두 삭제) - 관리자 기능에서 사용
    void deleteByPostIdIn(List<Long> postIds);

    // 특정 post 의 Comment 삭제(사용자 ROLE)
    void deleteByPostId(Long postId);
}
