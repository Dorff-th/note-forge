package dev.noteforge.knowhub.post.controller;


import dev.noteforge.knowhub.tag.dto.TagResponse;
import dev.noteforge.knowhub.tag.service.PostTagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostControllerTest {

    @Autowired
    private PostTagService postTagService;

    @DisplayName("특정 게시물의 태그목록을 조회한다.")
    @Test
    void testGetTags() {
        Long id = 142L;

        List<TagResponse> tags = postTagService.getByPostId(id)
                .stream()
                .map(tagDTO ->
                        new TagResponse(tagDTO.getPostTag().getTag().getId(), tagDTO.getName())
                )
                .collect(Collectors.toList());

        tags.forEach(tag->{System.out.println(tag);});
    }

}