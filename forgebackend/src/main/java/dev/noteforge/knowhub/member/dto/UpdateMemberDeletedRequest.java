package dev.noteforge.knowhub.member.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 관리자 기능에서 사용자 탈퇴여부 변경을 요청하는 DTO
 */
@Getter
@Setter
public class UpdateMemberDeletedRequest {
    private Boolean deleted; // true or false
}