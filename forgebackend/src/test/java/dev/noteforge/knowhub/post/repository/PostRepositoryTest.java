package dev.noteforge.knowhub.post.repository;

import dev.noteforge.knowhub.attachment.enums.UploadType;
import dev.noteforge.knowhub.post.dto.PostDTO;
import dev.noteforge.knowhub.post.dto.PostDetailDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("게시글 목록 DTO 조회 테스트")
    void testFindAllPosts() {
        // given
        PageRequest pageable = PageRequest.of(0, 5); // 첫 페이지, 5개씩 조회

        // when
        Page<PostDTO> result = postRepository.findAllPosts(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isNotEmpty();

        System.out.println("===== 게시글 목록 =====");
        result.forEach(dto -> {
            System.out.println("ID: " + dto.getId());
            System.out.println("Title: " + dto.getTitle());
            System.out.println("Category: " + dto.getCategoryName());
            System.out.println("Writer: " + dto.getUsername() + " (" + dto.getNickname() + ")");
            System.out.println("댓글 수: " + dto.getCommentCount());
            System.out.println("첨부 수: " + dto.getAttachmentCount());
            System.out.println("------------------------");
        });

        // 간단 검증 (예: 조회된 모든 게시글 ID는 null이 아님)
        assertThat(result.getContent().stream().allMatch(dto -> dto.getId() != null)).isTrue();
    }

    @Test
    @DisplayName("게시글 목록 DTO 조회 테스트")
    void testFindAllPostsWithTag() {
        // given
        PageRequest pageable = PageRequest.of(0, 5); // 첫 페이지, 5개씩 조회

        // when
        Page<PostDTO> result = postRepository.findAllPostsByTag("spring", pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isNotEmpty();

        System.out.println("===== 게시글 목록 =====");
        result.forEach(dto -> {
            System.out.println("ID: " + dto.getId());
            System.out.println("Title: " + dto.getTitle());
            System.out.println("Category: " + dto.getCategoryName());
            System.out.println("Writer: " + dto.getUsername() + " (" + dto.getNickname() + ")");
            System.out.println("댓글 수: " + dto.getCommentCount());
            System.out.println("첨부 수: " + dto.getAttachmentCount());
            System.out.println("------------------------");
        });

        // 간단 검증 (예: 조회된 모든 게시글 ID는 null이 아님)
        assertThat(result.getContent().stream().allMatch(dto -> dto.getId() != null)).isTrue();
    }

    @Test
    @DisplayName("게시글 상세 조회 DTO 테스트")
    void testFindPostDetail() {
        // given: 테스트할 게시글 ID (테스트 DB에 존재해야 함)
        Long postId = 104L;

        // when
        Optional<PostDetailDTO> result = postRepository.findPostDetail(postId);

        // then
        assertThat(result).isPresent();
        PostDetailDTO dto = result.get();

        System.out.println("===== PostDetailDTO =====");
        System.out.println("ID: " + dto.getId());
        System.out.println("Title: " + dto.getTitle());
        System.out.println("Content: " + dto.getContent());
        System.out.println("Category: " + dto.getCategoryName());
        System.out.println("Member Username: " + dto.getUsername());
        System.out.println("Member Nickname: " + dto.getNickname());

        // 간단한 검증
        assertThat(dto.getId()).isEqualTo(postId);
        assertThat(dto.getTitle()).isNotBlank();
    }


}