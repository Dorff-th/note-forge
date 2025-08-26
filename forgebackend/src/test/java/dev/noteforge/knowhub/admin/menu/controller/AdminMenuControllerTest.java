package dev.noteforge.knowhub.admin.menu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.noteforge.knowhub.menu.domain.Menu;
import dev.noteforge.knowhub.menu.dto.MenuRequest;
import dev.noteforge.knowhub.menu.repository.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class AdminMenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clear() {
        menuRepository.deleteAll();
    }

    @Test
    void create_menu() throws Exception {
        MenuRequest request = MenuRequest.builder()
                .name("회원관리")
                .path("/admin/members")
                .role(Menu.Role.ADMIN)
                .sortOrder(1)
                .build();

        mockMvc.perform(post("/api/admin/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("회원관리"));
    }

    @Test
    void get_all_menus() throws Exception {
        menuRepository.save(Menu.builder()
                .name("홈")
                .path("/")
                .role(Menu.Role.PUBLIC)
                .sortOrder(1)
                .build());

        mockMvc.perform(get("/api/admin/menus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("홈"));
    }

    @Test
    void update_menu() throws Exception {
        Menu menu = menuRepository.save(Menu.builder()
                .name("OldName")
                .path("/old")
                .role(Menu.Role.ADMIN)
                .sortOrder(1)
                .build());

        MenuRequest update = MenuRequest.builder()
                .name("NewName")
                .path("/new")
                .role(Menu.Role.ADMIN)
                .sortOrder(2)
                .build();

        mockMvc.perform(put("/api/admin/menus/" + menu.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("NewName"));
    }

    @Test
    void delete_menu() throws Exception {
        Menu menu = menuRepository.save(Menu.builder()
                .name("삭제대상")
                .path("/delete")
                .role(Menu.Role.ADMIN)
                .sortOrder(1)
                .build());

        mockMvc.perform(delete("/api/admin/menus/" + menu.getId()))
                .andExpect(status().isOk());

        assertThat(menuRepository.findById(menu.getId())).isEmpty();
    }
}