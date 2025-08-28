package dev.noteforge.knowhub.menu.dto;

import dev.noteforge.knowhub.common.enums.RoleType;
import dev.noteforge.knowhub.menu.domain.Menu;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 *  매뉴 호출 응답 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuResponse {
    private Long id;
    private Long parentId;
    private int sortOrder;
    private String name;
    private String path;
    private RoleType role;
    private boolean active;
    private List<MenuResponse> children = new ArrayList<>();

    public static MenuResponse fromEntity(Menu menu) {
        return MenuResponse.builder()
                .id(menu.getId())
                .parentId(menu.getParentId())
                .name(menu.getName())
                .path(menu.getPath())
                .role(menu.getRole())
                .sortOrder(menu.getSortOrder())
                .active(menu.isActive())
                .build();
    }

}

