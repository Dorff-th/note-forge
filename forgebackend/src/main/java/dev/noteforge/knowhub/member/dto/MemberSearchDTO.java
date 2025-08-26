package dev.noteforge.knowhub.member.dto;

import lombok.Data;

/**
 *  관리자화면에서 사용자 조회 요청 DTO
 */
@Data
public class MemberSearchDTO {
    private String email;
    private String nickname;
    private String role;      // USER / ADMIN
    private String tab;       // active / inactive
}
