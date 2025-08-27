package dev.noteforge.knowhub.menu.service;

import dev.noteforge.knowhub.common.enums.RoleType;
import dev.noteforge.knowhub.menu.dto.MenuResponse;
import dev.noteforge.knowhub.menu.dto.MenuTreeDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    @DisplayName("권한에 따른 메뉴 호출 테스트")
    @Test
    void testGetMenu() {

        //RoleType role = RoleType.ADMIN;
        RoleType role = RoleType.ADMIN;

        List<MenuTreeDTO> menus =  menuService.getMenusByRole(role);

        System.out.println(menus.size());

        menus.forEach(menu->{System.out.println(menu);});

    }


}