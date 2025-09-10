package dev.noteforge.knowhub.post.controller;

import dev.noteforge.knowhub.common.dto.PageRequestDTO;
import dev.noteforge.knowhub.common.dto.PageResponseDTO;
import dev.noteforge.knowhub.member.domain.Member;
import dev.noteforge.knowhub.member.security.MemberDetails;
import dev.noteforge.knowhub.post.domain.Post;
import dev.noteforge.knowhub.post.dto.PostDTO;
import dev.noteforge.knowhub.post.dto.PostDetailDTO;
import dev.noteforge.knowhub.post.dto.PostRequestDTO;
import dev.noteforge.knowhub.post.dto.PostUpdateDTO;
import dev.noteforge.knowhub.post.service.PostService;
import dev.noteforge.knowhub.tag.dto.TagDTO;

import dev.noteforge.knowhub.tag.dto.TagResponse;
import dev.noteforge.knowhub.tag.service.PostTagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@Slf4j
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

        Optional<PostDetailDTO> resultDTO = postService.getPost(id);

        return resultDTO.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
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

    //신규 등록 요청
    @PostMapping
    public ResponseEntity<?> createPost(
            @AuthenticationPrincipal MemberDetails loginUser,
            @Valid @ModelAttribute PostRequestDTO dto
    ) {
        Member member = loginUser.getMember();
        Post saved = postService.createPost(dto, member);

        return ResponseEntity.ok(saved.getId()); // 일단 ID만 반환
    }

    // ✅ 수정 (PUT) 요청
    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePost(
            @AuthenticationPrincipal MemberDetails loginUser,
            @ModelAttribute PostUpdateDTO dto
    ) {
        // 1. 게시글 존재 여부 확인
        Post post = postService.getPostById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Post가 존재하지 않습니다!"));

        // 2. 권한 체크 (본인만 수정 가능)
        if (!post.getMember().getId().equals(loginUser.getId())) {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        // 3. 작성자 정보 세팅
        dto.setMemberId(loginUser.getId());

        // 4. 업데이트 수행
        Post updated = postService.editPost(dto);
        // 5. postId 반환
        return ResponseEntity.ok(updated.getId());
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

}
