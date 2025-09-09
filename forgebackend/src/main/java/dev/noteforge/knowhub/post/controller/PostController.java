package dev.noteforge.knowhub.post.controller;

import dev.noteforge.knowhub.common.dto.PageRequestDTO;
import dev.noteforge.knowhub.common.dto.PageResponseDTO;
import dev.noteforge.knowhub.member.domain.Member;
import dev.noteforge.knowhub.member.security.MemberDetails;
import dev.noteforge.knowhub.post.domain.Post;
import dev.noteforge.knowhub.post.dto.PostDTO;
import dev.noteforge.knowhub.post.dto.PostDetailDTO;
import dev.noteforge.knowhub.post.dto.PostRequestDTO;
import dev.noteforge.knowhub.post.service.PostService;
import dev.noteforge.knowhub.tag.dto.TagDTO;

import dev.noteforge.knowhub.tag.dto.TagResponse;
import dev.noteforge.knowhub.tag.service.PostTagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final PostTagService postTagService;

    /**
     * 게시글 목록 조회 (페이징 포함)
     */
    @GetMapping
    public PageResponseDTO<PostDTO> getPosts(PageRequestDTO requestDTO) {
        return postService.getPostList(requestDTO);
    }

    /**
     * 특정태그가 있는 게시글 목록 조회(페이징 포함)
     */
    @GetMapping("/tags/{tagName}")
    public PageResponseDTO<PostDTO> getPostsByTag(@PathVariable("tagName") String tagName, PageRequestDTO requestDTO) {
        return postService.getPostListByTag(tagName, requestDTO);
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

    /**
     * 게시글에 포함된 태그 목록 조회
     */
    @GetMapping("/{id}/tags")
    public ResponseEntity<List<TagResponse>> getPostTags(@PathVariable("id") Long id) {

        List<TagResponse> tags = postTagService.getByPostId(id)
                .stream()
                .map(tagDTO ->
                    new TagResponse(tagDTO.getPostTag().getTag().getId(), tagDTO.getName())
                )
                .collect(Collectors.toList());
        return ResponseEntity.ok(tags);
    }

    @PostMapping
    public ResponseEntity<?> createPost(
            @AuthenticationPrincipal MemberDetails loginUser,
            @Valid @ModelAttribute PostRequestDTO dto
    ) {
        Member member = loginUser.getMember();
        Post saved = postService.createPost(dto, member);

        return ResponseEntity.ok(saved.getId()); // 일단 ID만 반환
    }

}
