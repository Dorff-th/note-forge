package dev.noteforge.knowhub.menu.controller;

import dev.noteforge.knowhub.common.enums.RoleType;
import dev.noteforge.knowhub.member.security.MemberDetails;
import dev.noteforge.knowhub.menu.dto.MenuTreeDTO;
import dev.noteforge.knowhub.menu.service.MenuService;
import dev.noteforge.knowhub.menu.util.MenuTreeBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public List<MenuTreeDTO> getMenus(@AuthenticationPrincipal MemberDetails loginUser) {
        RoleType roleType = loginUser.getMember().getRole();
        List<MenuTreeDTO> flatList = menuService.getMenusByRole(roleType);
        return MenuTreeBuilder.buildTree(flatList); // 유틸 클래스 호출
    }
}
