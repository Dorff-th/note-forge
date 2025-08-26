package dev.noteforge.knowhub.menu.util;


import dev.noteforge.knowhub.menu.domain.Menu;
import dev.noteforge.knowhub.menu.dto.MenuResponse;

import java.util.*;
import java.util.stream.Collectors;

public class MenuTreeConverter {

    /**
     * flat list 형태의 메뉴를 트리 구조로 변환
     */
    public static List<MenuResponse> buildTree(List<Menu> menus) {
        // 1. 모든 메뉴를 id → MenuResponse 로 맵핑
        Map<Long, MenuResponse> map = menus.stream()
                .collect(Collectors.toMap(
                        Menu::getId,
                        m -> new MenuResponse(
                                m.getId(),
                                m.getParentId(),
                                m.getSortOrder(),
                                m.getName(),
                                m.getPath(),
                                m.getRole(),
                                new ArrayList<>()
                        )
                ));

        // 2. 루트 메뉴 수집용 리스트
        List<MenuResponse> roots = new ArrayList<>();

        // 3. parentId 기반으로 트리 구성
        for (Menu menu : menus) {
            MenuResponse current = map.get(menu.getId());

            if (menu.getParentId() == null) {
                // parentId 없으면 루트
                roots.add(current);
            } else {
                // parentId 있으면 부모의 children에 추가
                MenuResponse parent = map.get(menu.getParentId());
                if (parent != null) {
                    parent.getChildren().add(current);
                }
            }
        }

        // 4. 정렬 (sortOrder 기준, 없으면 id 기준)
        sortTree(roots);

        return roots;
    }

    private static void sortTree(List<MenuResponse> nodes) {
        nodes.sort(Comparator.comparing(MenuResponse::getId)); // 필요시 sortOrder로 변경 가능
        for (MenuResponse node : nodes) {
            if (node.getChildren() != null && !node.getChildren().isEmpty()) {
                sortTree(node.getChildren());
            }
        }
    }
}
