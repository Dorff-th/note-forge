package dev.noteforge.knowhub.menu.domain;

import dev.noteforge.knowhub.common.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "menu")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 200)
    private String path;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RoleType role;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // == 편의 메서드 == //
    public Menu(String name, String path, RoleType role, Long parentId, Integer sortOrder, boolean isActive) {

        this.name = name;
        this.path = path;
        this.role = role;
        this.parentId = parentId;
        this.sortOrder = sortOrder;
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String name, String path, RoleType role, Long parentId, Integer sortOrder, boolean isActive) {
        this.name = name;
        this.path = path;
        this.role = role;
        this.parentId = parentId;
        this.sortOrder = sortOrder;
        this.updatedAt = LocalDateTime.now();
    }
}
