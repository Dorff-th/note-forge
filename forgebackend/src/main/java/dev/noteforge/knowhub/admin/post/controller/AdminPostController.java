package dev.noteforge.knowhub.admin.post.controller;

import dev.noteforge.knowhub.admin.post.service.AdminPostService;
import dev.noteforge.knowhub.common.dto.PageRequestDTO;
import dev.noteforge.knowhub.common.dto.PageResponseDTO;
import dev.noteforge.knowhub.post.dto.PostDTO;
import dev.noteforge.knowhub.post.dto.PostDetailDTO;
import dev.noteforge.knowhub.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 게시물관리 Api Controller
 */
@RestController
@RequestMapping("/api/admin/posts")
@RequiredArgsConstructor
public class AdminPostController {

    private final AdminPostService adminPostService;

    private final PostService postService;

    /**
     * 게시글 목록 조회 (페이징 포함)
     */
    @GetMapping
    public PageResponseDTO<PostDTO> getPosts(PageRequestDTO requestDTO) {
        return postService.getPostList(requestDTO);
    }

    /**
     * 게시글 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostDetailDTO> getPost(@PathVariable("id") Long id) {
        return postService.getPost(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //단일 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long id) {
        // 일괄삭제 서비스 재사용
        adminPostService.deletePosts(List.of(id));
        return ResponseEntity.noContent().build();
    }

    //일괄 삭제
    @DeleteMapping
    public ResponseEntity<Void> deletePosts(@RequestBody List<Long> postIds) {
        adminPostService.deletePosts(postIds);
        return ResponseEntity.noContent().build();
    }
}
