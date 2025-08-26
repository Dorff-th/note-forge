package dev.noteforge.knowhub.post.repository;

import dev.noteforge.knowhub.attachment.enums.UploadType;
import dev.noteforge.knowhub.post.domain.Post;
import dev.noteforge.knowhub.post.dto.PostDTO;
import dev.noteforge.knowhub.post.dto.PostDetailDTO;
import dev.noteforge.knowhub.post.dto.PostUpdateDTO;
import dev.noteforge.knowhub.post.domain.Post;
import dev.noteforge.knowhub.post.dto.PostUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     *  게시물 목록 조회 쿼리
     * @param pageable
     * @return Page<PostDTO>
     */
    @Query("SELECT new dev.noteforge.knowhub.post.dto.PostDTO(" +
            "p.id, p.title, p.createdAt, c.name, m.username, m.id, " +
            "COUNT(cm.id), m.nickname, " +
            "COUNT(CASE WHEN a.uploadType = 'ATTACHMENT' THEN a.id END)) " +
            "FROM Post p " +
            "LEFT JOIN p.category c " +
            "LEFT JOIN p.member m " +
            "LEFT JOIN p.comments cm " +
            "LEFT JOIN p.attachments a " +
            "GROUP BY p.id, p.title, p.createdAt, c.name, m.username, m.id, m.nickname " +
            "ORDER BY p.id DESC")
    Page<PostDTO> findAllPosts(Pageable pageable);

    // 특정 태그 조회
    @Query("SELECT new dev.noteforge.knowhub.post.dto.PostDTO(" +
            "p.id, p.title, p.createdAt, c.name, m.username, m.id, " +
            "COUNT(cm.id), m.nickname, " +
            "COUNT(CASE WHEN a.uploadType = 'ATTACHMENT' THEN a.id END)) " +
            "FROM Post p " +
            "LEFT JOIN p.category c " +
            "LEFT JOIN p.member m " +
            "LEFT JOIN p.comments cm " +
            "LEFT JOIN p.attachments a " +
            "JOIN p.postTags pt " +
            "JOIN pt.tag t " +
            "WHERE t.name = :tagName " +
            "GROUP BY p.id, p.title, p.createdAt, c.name, m.username, m.id, m.nickname " +
            "ORDER BY p.id DESC")
    Page<PostDTO> findAllPostsByTag(@Param("tagName") String tagName, Pageable pageable);

    /**
     *  게시물 상세 조회
     * @param postId
     * @return Optional<PostDetailDTO>
     */
    @Query("SELECT new dev.noteforge.knowhub.post.dto.PostDetailDTO(" +
            "p.id, p.title, p.content, p.createdAt, p.updatedAt, " +
            "c.name, m.id, m.username, c.id, m.nickname) " +
            "FROM Post p " +
            "LEFT JOIN p.category c " +
            "LEFT JOIN p.member m " +
            "WHERE p.id = :postId " +
            "GROUP BY p.id, p.title, p.content, p.createdAt, p.updatedAt, " +
            "c.name, m.id, m.username, c.id, m.nickname")
    Optional<PostDetailDTO> findPostDetail(@Param("postId") Long postId);

    /**
     * 게시물을 수정한다.
     * @param dto
     * @return int
     */
    @Modifying
    @Transactional
    @Query("UPDATE Post a SET a.title = :#{#dto.title}, a.content =:#{#dto.content}, a.category.id =:#{#dto.categoryId}, a.updatedAt =:#{#dto.updatedAt} " +
            "WHERE a.id = :#{#dto.id}")
    int updatePostById(@Param("dto") PostUpdateDTO dto);

    //관리자 화면에서 여러 Post 일괄삭제
    void deleteByIdIn(List<Long> postIds);

}
