package dev.noteforge.knowhub.tag.dto;

import dev.noteforge.knowhub.tag.domain.PostTag;
import lombok.Data;

/*
PostId 로 PostTag테이블과 Tag테이블을 조인하여 얻은결과를 담는 DTO
 */
@Data
public class TagDTO {
    private PostTag postTag;
    private String name;

    public TagDTO(PostTag postTag, String name) {
        this.postTag = postTag;
        this.name = name;
    }
}
