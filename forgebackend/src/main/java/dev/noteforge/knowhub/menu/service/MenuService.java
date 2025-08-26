package dev.noteforge.knowhub.menu.service;

import dev.noteforge.knowhub.common.enums.RoleType;

import dev.noteforge.knowhub.menu.domain.Menu;
import dev.noteforge.knowhub.menu.dto.MenuResponse;
import dev.noteforge.knowhub.menu.repository.MenuRepository;
import dev.noteforge.knowhub.menu.util.MenuTreeConverter;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    /**
     * role 조건에 맞는 메뉴 트리를 조회
     * @param role 요청한 Role (null이면 전체)
     */
    public List<MenuResponse> getMenusByRole(RoleType role) {
        List<Menu> menus;

        if (role == null) {
            // 전체 메뉴 (활성된 것만)
            menus = menuRepository.findByIsActiveTrueOrderBySortOrderAsc();
        } else {
            // 지정한 Role 이상 접근 가능한 메뉴만 필터링
            menus = menuRepository.findByRoleAndIsActiveTrueOrderBySortOrderAsc(role);
        }

        return MenuTreeConverter.buildTree(menus);
    }
}
