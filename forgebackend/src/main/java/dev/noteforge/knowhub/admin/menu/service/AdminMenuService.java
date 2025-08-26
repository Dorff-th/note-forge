package dev.noteforge.knowhub.admin.menu.service;

import dev.noteforge.knowhub.menu.domain.Menu;
import dev.noteforge.knowhub.menu.dto.MenuRequest;
import dev.noteforge.knowhub.menu.dto.MenuResponse;
import dev.noteforge.knowhub.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 메뉴관리 service
 */
@Service
@RequiredArgsConstructor
public class AdminMenuService {

    private final MenuRepository menuRepository;

    public List<MenuResponse> getAllMenus() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public MenuResponse getMenu(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Menu not found: " + id));
        return MenuResponse.fromEntity(menu);
    }

    public void createMenu(MenuRequest request) {
        Menu menu = new Menu(request.getName(), request.getPath(), request.getRole(), request.getParentId(), request.getSortOrder(), request.isActive());
        menuRepository.save(menu);
    }

    public void updateMenu(Long id, MenuRequest request) {
        Menu menu = menuRepository.findById(id).orElseThrow();
        menu.update(request.getName(), request.getPath(), request.getRole(), request.getParentId(), request.getSortOrder(), request.isActive());
    }

    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }
}
