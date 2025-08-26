package dev.noteforge.knowhub.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageRequestDTO {

    @Builder.Default
    private int page = 1; // 기본 1페이지

    @Builder.Default
    private int size = 10; // 기본 페이지 크기

    private String sort = "id"; // 정렬 기준 (예: createdAt)

    private SortDirection direction = SortDirection.DESC;

    public Pageable getPageable() {
        return PageRequest.of(
                Math.max(this.page - 1, 0),  // 1 -> 0, 2 -> 1 ...
                this.size,
                Sort.by(this.direction.toString(), this.sort)
        );
    }

    //MyBatis 에서 페이지 번호 - index는 0부터 시작
    public int getOffset() {
        return (page - 1) * size;
    }
}
