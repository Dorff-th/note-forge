package dev.noteforge.knowhub.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 상세 검색(필터) 요청용 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchFilterDTO {
    private String keyword;
    private Boolean titleChecked;
    private Boolean contentChecked;
    private Boolean commentChecked;
    private Long categoryId;    // <- Category Id 값을 검색 조건에 포함할 경우 필요
    private LocalDate startDate;
    private LocalDate endDate;


}

