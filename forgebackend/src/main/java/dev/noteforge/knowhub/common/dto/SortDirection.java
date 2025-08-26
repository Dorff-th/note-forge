package dev.noteforge.knowhub.common.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.data.domain.Sort;

public enum SortDirection {
    ASC, DESC;

    @JsonCreator
    public static SortDirection from(String value) {
        try {
            return SortDirection.valueOf(value.toUpperCase());
        } catch (Exception e) {
            return DESC; // 또는 throw new IllegalArgumentException("잘못된 정렬 값");
        }
    }

    public Sort.Direction toSpringDirection() {
        return Sort.Direction.valueOf(this.name());
    }
}

