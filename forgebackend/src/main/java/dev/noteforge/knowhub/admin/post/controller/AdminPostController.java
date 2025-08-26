package dev.noteforge.knowhub.admin.post.controller;

import dev.noteforge.knowhub.admin.post.service.AdminPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 게시물관리 Api Controller
 */
@RestController
@RequestMapping("/api/admin/posts")
@RequiredArgsConstructor
public class AdminPostController {

    private final AdminPostService adminPostService;

    @DeleteMapping
    public ResponseEntity<Void> deletePosts(@RequestBody List<Long> postIds) {
        adminPostService.deletePosts(postIds);
        return ResponseEntity.noContent().build();
    }
}
