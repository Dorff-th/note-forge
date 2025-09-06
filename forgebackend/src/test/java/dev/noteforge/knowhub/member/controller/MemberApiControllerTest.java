package dev.noteforge.knowhub.member.controller;

import dev.noteforge.knowhub.common.enums.RoleType;
import dev.noteforge.knowhub.member.domain.Member;
import dev.noteforge.knowhub.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MemberApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*@BeforeEach
    void setUp() {
        memberRepository.deleteAll();

        Member member = Member.builder()
                .username("testuser@test.com")
                .password(passwordEncoder.encode("password123"))
                .nickname("테스트유저")
                .role(RoleType.USER)
                .profileImageUrl(null) // default 이미지는 프론트에서 처리
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        memberRepository.save(member);
    }*/

    @Test
    @WithMockUser(username = "bob@test.com", roles = {"USER"})
    void 내프로필조회_성공() throws Exception {
        mockMvc.perform(get("/api/members/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("bob@test.com"));
        // 필요하면 nickname, role 같은 다른 필드도 추가 검증 가능
    }
}