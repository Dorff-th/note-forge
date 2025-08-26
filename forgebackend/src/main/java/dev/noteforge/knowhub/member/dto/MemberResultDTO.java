package dev.noteforge.knowhub.member.dto;

import dev.noteforge.knowhub.member.enums.MemberStatus;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 관리자 화면의 사용자 정보를 반환하는 DTO
 */
@Data
@ToString
public class MemberResultDTO {
    private Long id;
    private String username;
    private String email;
    private String nickname;
    private String role;      // USER / ADMIN
    private MemberStatus status;
    private boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
