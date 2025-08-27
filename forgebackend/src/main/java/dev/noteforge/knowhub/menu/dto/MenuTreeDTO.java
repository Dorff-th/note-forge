package dev.noteforge.knowhub.menu.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MenuTreeDTO {
    private Long id;
    private Long parentId;
    private String name;
    private String path;
    private String role;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private String hierarchyPath;
    private Integer level;

    // 트리 구조 변환용
    private List<MenuTreeDTO> children;
}
