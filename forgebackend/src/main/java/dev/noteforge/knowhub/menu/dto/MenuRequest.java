package dev.noteforge.knowhub.menu.dto;

import dev.noteforge.knowhub.common.enums.RoleType;
import dev.noteforge.knowhub.menu.domain.Menu;
import lombok.*;

/**
 * 관리자 화면에서 메뉴 추가 요청 DTO
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MenuRequest {
    private Long parentId;
    private String name;
    private String path;
    private RoleType role;
    private Integer sortOrder;
    private boolean active;
}
