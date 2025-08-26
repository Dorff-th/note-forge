package dev.noteforge.knowhub.category.repository;

import dev.noteforge.knowhub.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    //기본 카테고리만 제외하고 조회(미분류 카테고리는  기존 카테고리 삭제시, 삭제된 카테고리의 Post가 미분류 즉 기본로 들어감)
    List<Category> findAllByDefaultCategoryFalse();

    // 기본 카테고리만 조회
    Optional<Category> findByDefaultCategoryTrue();

    // 이름 중복 체크 (관리자 페이지에서 신규 카테고리 추가할때 미리 동일한 카테고리 이름이 있는지 조회)
    boolean existsByName(String name);
}
