package dev.noteforge.knowhub.admin.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *  관리자 화면에서 전체 통계수치를 반환하기 위한 DTO
 */
@Data
@AllArgsConstructor
public class AdminStatsAllDTO {
    private long memberCount;
    private long postCount;
    private long commentCount;
    private long attachmentCount;
    private long editorImageCount;
    private long categoryCount;
    private long tagCount;
}
