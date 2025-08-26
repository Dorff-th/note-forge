package dev.noteforge.knowhub.admin.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
class AdminMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("회원 상세조회 API 성공")
    void getMemberDetailApi() throws Exception {
        mockMvc.perform(get("/api/admin/members/{id}", 28L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(28L))
                .andExpect(jsonPath("$.username").isNotEmpty());
    }

    @Test
    @DisplayName("회원 상태변경 API 성공")
    void updateStatusApi() throws Exception {
        String json = """
                { "status": "INACTIVE" }
                """;

        mockMvc.perform(patch("/api/admin/members/{id}/status", 28L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent());

        // 변경 확인
        mockMvc.perform(get("/api/admin/members/{id}", 28L))
                .andExpect(jsonPath("$.status").value("INACTIVE"));
    }

    @Test
    @DisplayName("회원 탈퇴여부 변경 API 성공")
    void updateDeletedApi() throws Exception {
        String json = """
                { "deleted": true }
                """;

        mockMvc.perform(patch("/api/admin/members/{id}/deleted", 28L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNoContent());

        // 변경 확인
        mockMvc.perform(get("/api/admin/members/{id}", 28L))
                .andExpect(jsonPath("$.deleted").value(true));
    }

}