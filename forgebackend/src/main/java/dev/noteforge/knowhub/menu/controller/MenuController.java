package dev.noteforge.knowhub.menu.controller;

import dev.noteforge.knowhub.common.enums.RoleType;
import dev.noteforge.knowhub.menu.dto.MenuResponse;
import dev.noteforge.knowhub.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// MenuController.java
@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // role에 맞는 메뉴 트리 조회
    @GetMapping
    public List<MenuResponse> getMenus(@RequestParam(name="role", required = false) RoleType role) {
        return menuService.getMenusByRole(role);
    }
}

