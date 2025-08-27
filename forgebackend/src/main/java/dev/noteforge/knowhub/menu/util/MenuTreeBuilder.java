package dev.noteforge.knowhub.menu.util;

import dev.noteforge.knowhub.menu.dto.MenuTreeDTO;

import java.util.*;

public class MenuTreeBuilder {

    private MenuTreeBuilder() {
        // static util class → 인스턴스화 방지
    }

    public static List<MenuTreeDTO> buildTree(List<MenuTreeDTO> flatList) {
        Map<Long, MenuTreeDTO> map = new HashMap<>();
        List<MenuTreeDTO> roots = new ArrayList<>();

        for (MenuTreeDTO dto : flatList) {
            dto.setChildren(new ArrayList<>());
            map.put(dto.getId(), dto);
        }

        for (MenuTreeDTO dto : flatList) {
            if (dto.getParentId() != null) {
                MenuTreeDTO parent = map.get(dto.getParentId());
                if (parent != null) {
                    parent.getChildren().add(dto);
                }
            } else {
                roots.add(dto);
            }
        }

        // sort_order 기준 정렬 (null은 뒤로 보냄)
        roots.sort(Comparator.comparing(MenuTreeDTO::getSortOrder, Comparator.nullsLast(Integer::compareTo)));
        map.values().forEach(node ->
                node.getChildren().sort(Comparator.comparing(MenuTreeDTO::getSortOrder, Comparator.nullsLast(Integer::compareTo)))
        );

        return roots;
    }
}
