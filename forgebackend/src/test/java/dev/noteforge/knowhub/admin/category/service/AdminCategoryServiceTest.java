package dev.noteforge.knowhub.admin.category.service;

import dev.noteforge.knowhub.admin.category.service.AdminCategoryService;

import dev.noteforge.knowhub.category.domain.Category;
import dev.noteforge.knowhub.category.repository.CategoryRepository;

import dev.noteforge.knowhub.post.domain.Post;
import dev.noteforge.knowhub.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AdminCategoryServiceTest {

    @Autowired
    private AdminCategoryService adminCategoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    @Transactional
    @Rollback(false) // 실제 DB에 반영
    @DisplayName("카테고리 삭제 시 Post는 기본 카테고리로 이동한다")
    void deleteCategoryAndMovePosts() {
        // given: 삭제할 카테고리 ID
        Long categoryIdToDelete = 5L; // 👉 실제 더미 데이터 기준으로 테스트할 ID 넣으세요

        // 기본 카테고리
        Category defaultCategory = categoryRepository.findById(categoryIdToDelete)
                .orElseThrow(() -> new IllegalArgumentException("카테고리 없음"));

        // 삭제 전, 해당 카테고리에 속한 Post 목록 확인
        List<Post> beforePosts = postRepository.findAll()
                .stream()
                .filter(p -> p.getCategory().getId().equals(categoryIdToDelete))
                .toList();

        System.out.println("삭제 전 게시글 수 = " + beforePosts.size());

        // when
        adminCategoryService.deleteCategory(categoryIdToDelete);

        // then: 삭제 후, 해당 카테고리에 있던 Post가 기본 카테고리로 이동했는지 확인
        List<Post> afterPosts = postRepository.findAll()
                .stream()
                .filter(p -> p.getCategory().getId().equals(defaultCategory.getId()))
                .toList();

        System.out.println("삭제 후 기본 카테고리로 이동한 게시글 수 = " + afterPosts.size());

        assertThat(afterPosts.size()).isGreaterThanOrEqualTo(beforePosts.size());
        assertThat(categoryRepository.findById(categoryIdToDelete)).isEmpty();
    }
}
