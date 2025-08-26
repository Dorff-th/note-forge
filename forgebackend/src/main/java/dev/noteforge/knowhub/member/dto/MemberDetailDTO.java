package dev.noteforge.knowhub.member.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 *  관리자 화면에서 사용자정보 상세보기를 반환하는 DTO
 */
@Getter
@Setter
@ToString
public class MemberDetailDTO {
    private Long id;
    private String username;
    private String email;
    private String nickname;
    private String role;
    private String status;   // Enum 대신 문자열 매핑
    private Boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
