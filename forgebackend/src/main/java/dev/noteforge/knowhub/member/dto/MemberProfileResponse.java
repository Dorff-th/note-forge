package dev.noteforge.knowhub.member.dto;

import dev.noteforge.knowhub.common.enums.RoleType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 사용자 개인 프로필 조회 응답용 DTO (/api/members/me)
 */
@Getter
@Builder
public class MemberProfileResponse {
    private String profileImageUrl;
    private String username;
    private String nickname;
    private RoleType role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
