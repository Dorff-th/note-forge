package dev.noteforge.knowhub.menu.repository;



import dev.noteforge.knowhub.common.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import dev.noteforge.knowhub.menu.domain.Menu;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    // 특정 권한(role)에 해당하는 모든 메뉴 조회
    List<Menu> findByIsActiveTrueOrderBySortOrderAsc();

    List<Menu> findByRoleAndIsActiveTrueOrderBySortOrderAsc(RoleType role);
}

