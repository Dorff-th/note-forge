package dev.noteforge.knowhub.attachment.repository;

import dev.noteforge.knowhub.attachment.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImageUploadRepository extends JpaRepository<Attachment, Long> {

    List<Attachment> findByPostId(Long postId);


    //에디터에 첨부된 이미지 파일 정보에 post id를 update하기 위해 tempKey값이 있는지 조회
    boolean existsByTempKey(String tempKey);

    // Post 가 저장될때 전송된 tempKey와 Attachment 에 저장된 tempKey가 동일하면 Attachement에 PostId 업데이트
    @Modifying
    @Transactional
    @Query("UPDATE Attachment a SET a.post.id = :postId WHERE a.tempKey = :tempKey")
    void updatePostIdByTempKey(@Param("postId") Long postId, @Param("tempKey") String tempKey);

    //Post id 조건에 맞는 Attachement 삭제 - Post 에서 엔티티 관계가 제대로 되어있다면 post 삭제할때 같은 post id가 있는 Attachement 도 같이 삭제
    /*@Modifying
    @Query("DELETE FROM Attachment a WHERE a.post.id = :postId")
    void deleteByPostId(@Param("postId") Long postId);*/

    //같은 temp_key에서 에디터에 남아있는 file_name을 제외한 attachement를 조회 (tempKey 로 조회)
    @Query("SELECT a FROM Attachment a WHERE a.tempKey = :tempKey AND a.fileName NOT IN :fileNames")
    List<Attachment> findUnusedImagesByTempKey(@Param("tempKey") String tempKey, @Param("fileNames") List<String> fileNames);

    @Modifying
    @Query("DELETE FROM Attachment a WHERE a.tempKey = :tempKey AND a.fileName IN :fileNames")
    void deleteByTempKeyAndStoredNamesByTempKey(@Param("tempKey") String tempKey,
                                       @Param("fileNames") List<String> fileNames);

    //같은 postId 에서 에디터에 남아있는 file_name을 제외한 attachement를 조회 (postId와 uplpadType가 EDITOR_IMAGE인것을 로 조회)
    @Query("SELECT a FROM Attachment a WHERE a.post.id = :postId AND a.fileName NOT IN :fileNames AND a.uploadType = 'EDITOR_IMAGE'")
    List<Attachment> findUnusedImagesByPostId(@Param("postId") Long postId, @Param("fileNames") List<String> fileNames);

    @Modifying
    @Query("DELETE FROM Attachment a WHERE a.post.id = :postId AND a.fileName IN :fileNames AND a.uploadType = 'EDITOR_IMAGE'")
    void deleteByTempKeyAndStoredNamesByPostId(@Param("postId") Long postId,
                                       @Param("fileNames") List<String> fileNames);


}
