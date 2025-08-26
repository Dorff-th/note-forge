package dev.noteforge.knowhub.post.service;

import dev.noteforge.knowhub.common.dto.PageRequestDTO;
import dev.noteforge.knowhub.common.dto.PageResponseDTO;
import dev.noteforge.knowhub.common.dto.SortDirection;
import dev.noteforge.knowhub.post.dto.PostDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    @DisplayName("Post 페이징 처리 테스트")
    void testPostsPaging() {

        SortDirection sortDirection = SortDirection.ASC; // 정렬 역순
        String sortColumn = "id";

        PageRequestDTO pageRequestDTO = new PageRequestDTO(1, 10, sortColumn, sortDirection);
        PageResponseDTO<PostDTO> result = postService.getPostList(pageRequestDTO);

        System.out.println(result.getTotalElements());
        result.getDtoList().forEach(postDTO -> System.out.println(postDTO));

    }

}