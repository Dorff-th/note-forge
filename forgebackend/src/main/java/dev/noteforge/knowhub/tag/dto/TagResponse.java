package dev.noteforge.knowhub.tag.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * React 프론트엔로 내려보내기 위한 TagDTO
 */
@ToString
@Getter
public class TagResponse {

    private Long id;     // 태그 ID
    private String name; // 태그 이름

    public TagResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
