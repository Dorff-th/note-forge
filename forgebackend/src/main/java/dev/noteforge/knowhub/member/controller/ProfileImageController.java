package dev.noteforge.knowhub.member.controller;

import dev.noteforge.knowhub.member.dto.ProfileImageResponse;
import dev.noteforge.knowhub.member.security.MemberDetails;
import dev.noteforge.knowhub.member.service.ProfileImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/members/me/profile-image")
@RequiredArgsConstructor
@Slf4j
public class ProfileImageController {

    private final ProfileImageService profileImageService;

    /**
     * 프로필 이미지 업로드 (또는 교체)
     */
    @PatchMapping
    public ResponseEntity<ProfileImageResponse> uploadProfileImage(
            @AuthenticationPrincipal MemberDetails loginUser,
            @RequestParam("file") MultipartFile file) throws IOException {

        Long memberId = loginUser.getId();
        String imageUrl = profileImageService.uploadProfileImage(memberId, file);

        return ResponseEntity.ok(new ProfileImageResponse(imageUrl));
    }

    /**
     * 프로필 이미지 삭제
     */
    @DeleteMapping
    public ResponseEntity<ProfileImageResponse> deleteProfileImage(
            @AuthenticationPrincipal MemberDetails loginUser) {

        Long memberId = loginUser.getId();
        profileImageService.deleteProfileImage(memberId);

        return ResponseEntity.ok(new ProfileImageResponse("/default.png"));
    }

    /**
     * 현재 사용자 프로필 이미지 조회
     */
    @GetMapping
    public ResponseEntity<ProfileImageResponse> getProfileImage(
            @AuthenticationPrincipal MemberDetails loginUser) {

        Long memberId = loginUser.getId();
        String imageUrl = profileImageService.getProfileImageUrl(memberId);

        return ResponseEntity.ok(new ProfileImageResponse(imageUrl));
    }
}
