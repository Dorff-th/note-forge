package dev.noteforge.knowhub.attachment.repository;

import dev.noteforge.knowhub.attachment.domain.Attachment;
import dev.noteforge.knowhub.attachment.enums.UploadType;
import dev.noteforge.knowhub.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    @Query("SELECT a FROM Attachment a WHERE a.post.id = :postId AND a.uploadType = :uploadType")
    List<Attachment> findByPostId(@Param("postId") Long postId, @Param("uploadType") UploadType uploadType);

    //Post 삭제될때 하위 Attachment 모두 삭제 - 관리자 기능
    void deleteByPostIdIn(List<Long> postIds);

    //특정 Post 삭제될때 Attachment 삭제
    void deleteByPostId(Long postId);
}
