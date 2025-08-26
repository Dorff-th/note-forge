package dev.noteforge.knowhub.member.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 관리자기능에서 사용자 상태 변경을 요청하는 DTO
 */
@Getter
@Setter
public class UpdateMemberStatusRequest {
    private String status;   // "ACTIVE" or "INACTIVE"
}
