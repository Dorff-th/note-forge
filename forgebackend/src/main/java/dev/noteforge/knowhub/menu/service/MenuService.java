package dev.noteforge.knowhub.menu.service;

import dev.noteforge.knowhub.common.enums.RoleType;
import dev.noteforge.knowhub.menu.dto.MenuTreeDTO;
import dev.noteforge.knowhub.menu.mapper.MenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuMapper menuMapper;

    /*public List<MenuTreeDTO> getMenusByRole(RoleType roleType) {
        List<String> roles = new ArrayList<>();
        roles.add(RoleType.PUBLIC.name()); // "PUBLIC"

        if (roleType == RoleType.USER || roleType == RoleType.ADMIN) {
            roles.add(RoleType.USER.name());
        }
        if (roleType == RoleType.ADMIN) {
            roles.add(RoleType.ADMIN.name());
        }

        return menuMapper.getMenuHierarchyByRoles(roles);
    }*/

    public List<MenuTreeDTO> getMenusByRole(RoleType roleType) {
        List<String> roles = resolveRolesForMenu(roleType);
        return menuMapper.getMenuHierarchyByRoles(roles);
    }

    private List<String> resolveRolesForMenu(RoleType roleType) {
        switch (roleType) {
            case ADMIN:
                return List.of(RoleType.ADMIN.name());
            case USER:
                return List.of(RoleType.USER.name(), RoleType.PUBLIC.name());
            default: // 비로그인 or 기타
                return List.of(RoleType.PUBLIC.name());
        }
    }
}

