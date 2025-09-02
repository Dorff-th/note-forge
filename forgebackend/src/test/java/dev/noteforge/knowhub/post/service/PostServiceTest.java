package dev.noteforge.knowhub.post.service;

import dev.noteforge.knowhub.common.dto.PageRequestDTO;
import dev.noteforge.knowhub.common.dto.PageResponseDTO;
import dev.noteforge.knowhub.common.dto.SortDirection;
import dev.noteforge.knowhub.post.dto.PostDTO;
import dev.noteforge.knowhub.post.dto.PostDetailDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

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

        PageRequestDTO pageRequestDTO = new PageRequestDTO(5, 10, sortColumn, sortDirection);
        PageResponseDTO<PostDTO> result = postService.getPostList(pageRequestDTO);

        System.out.println(result.getTotalElements());
        result.getDtoList().forEach(postDTO -> System.out.println(postDTO));

    }

    @Test
    @DisplayName("첨부파일이 있는 게시물 상세 조회 화면 테스트")
    void testPostDetailWithAttachment() {
        Long id = 118L;
        Optional<PostDetailDTO> result = postService.getPost(id);

        PostDetailDTO dto = result.get();

        System.out.println("===== PostDetailDTO =====");
        System.out.println("ID: " + dto.getId());
        System.out.println("Title: " + dto.getTitle());
        System.out.println("Content: " + dto.getContent());
        System.out.println("Category: " + dto.getCategoryName());
        System.out.println("Member Username: " + dto.getUsername());
        System.out.println("Member Nickname: " + dto.getNickname());

        if(dto.getAttachments().size() > 0) {
            dto.getAttachments().forEach(attachmentViewDTO -> {System.out.println(attachmentViewDTO.getOriginalName());});
        }
    }

}