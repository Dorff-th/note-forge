package dev.noteforge.knowhub.admin.member.controller;

import dev.noteforge.knowhub.admin.member.service.AdminMemberService;
import dev.noteforge.knowhub.member.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 사용자관리 Api Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/members")
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    @GetMapping
    public List<MemberResultDTO> getMembers(
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "nickname",required = false) String nickname,
            @RequestParam(name = "role",required = false) String role,
            @RequestParam(name = "tab",defaultValue = "active") String tab
    ) {
        MemberSearchDTO dto = new MemberSearchDTO();
        dto.setEmail(email);
        dto.setNickname(nickname);
        dto.setRole(role);
        dto.setTab(tab);

        return adminMemberService.getMembers(dto);
    }

    // 상세조회
    @GetMapping("/{id}")
    public ResponseEntity<MemberDetailDTO> getMemberDetail(@PathVariable("id") Long id) {
        return ResponseEntity.ok(adminMemberService.getMemberDetail(id));
    }

    // 회원 상태 변경
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable("id") Long id,
            @RequestBody UpdateMemberStatusRequest request) {
        adminMemberService.updateStatus(id, request.getStatus());
        return ResponseEntity.noContent().build();
    }

    // 회원 탈퇴 여부 변경
    @PatchMapping("/{id}/deleted")
    public ResponseEntity<Void> updateDeleted(
            @PathVariable("id") Long id,
            @RequestBody UpdateMemberDeletedRequest request) {
        adminMemberService.updateDeleted(id, request.getDeleted());
        return ResponseEntity.noContent().build();
    }
}
