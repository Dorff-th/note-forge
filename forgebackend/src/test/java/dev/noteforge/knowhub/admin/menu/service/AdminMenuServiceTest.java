package dev.noteforge.knowhub.admin.menu.service;

import dev.noteforge.knowhub.menu.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminMenuServiceTest {

    @Autowired
    private AdminMenuService adminMenuService;

    @DisplayName("관리자 화면에서 모든 메뉴를 조회한다.")
    @Test
    void testGetAllMenus() {
        List<MenuResponse> menus = adminMenuService.getAllMenus();
        menus.forEach(menuResponse -> {System.out.println(menuResponse);});
    }
}