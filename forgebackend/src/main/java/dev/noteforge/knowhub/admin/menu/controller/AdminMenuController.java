package dev.noteforge.knowhub.admin.menu.controller;

import dev.noteforge.knowhub.admin.menu.service.AdminMenuService;
import dev.noteforge.knowhub.menu.dto.MenuRequest;
import dev.noteforge.knowhub.menu.dto.MenuResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 메뉴관리 Api Controller
 */
@RestController
@RequestMapping("/api/admin/menus")
@RequiredArgsConstructor
public class AdminMenuController {

    private final AdminMenuService adminMenuService;

    @GetMapping
    public List<MenuResponse> getAllMenus() {
        return adminMenuService.getAllMenus();
    }

    @GetMapping("/{id}")
    public MenuResponse getMenu(@PathVariable("id") Long id) {
       return adminMenuService.getMenu(id);
    }

    @PostMapping
    public void createMenu(@RequestBody MenuRequest request) {
        adminMenuService.createMenu(request);
    }

    @PutMapping("/{id}")
    public void updateMenu(@PathVariable("id") Long id, @RequestBody MenuRequest request) {
        adminMenuService.updateMenu(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteMenu(@PathVariable("id") Long id) {
        adminMenuService.deleteMenu(id);
    }
}
