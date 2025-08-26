package dev.noteforge.knowhub.admin.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 관리자화면에서 오늘 등록한 사용자, 오늘등록한 게시물/코멘트만 조회 결과를 반환하는DTO
 */
@Data
@AllArgsConstructor
public class AdminStatsTodayDTO {
    private long todayNewMembers;
    private long todayNewPosts;
    private long todayNewComments;
}
