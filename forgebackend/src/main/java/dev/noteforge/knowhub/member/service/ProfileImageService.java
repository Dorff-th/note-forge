package dev.noteforge.knowhub.member.service;

import dev.noteforge.knowhub.member.repository.MemberRepository;
import dev.noteforge.knowhub.member.util.ProfileImageUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProfileImageService {

    private final ProfileImageUploadUtil profileImageUploadUtil;
    private final MemberRepository memberRepository;

    /**
     * 프로필 이미지 업로드 및 DB 반영
     */
    public String uploadProfileImage(Long memberId, MultipartFile file) throws IOException {
        // 1) 파일 저장 (300x300 PNG 변환)
        String imageUrl = profileImageUploadUtil.saveProfileImage(memberId, file);

        // 2) DB 업데이트
        int updated = memberRepository.updateProfileImageUrl(memberId, imageUrl);
        if (updated == 0) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다. memberId=" + memberId);
        }

        log.info("사용자 {} 프로필 이미지 변경 완료 → {}", memberId, imageUrl);
        return imageUrl;
    }

    /**
     * 프로필 이미지 삭제 및 DB 업데이트
     */
    public void deleteProfileImage(Long memberId) {
        // 1) 파일 삭제
        profileImageUploadUtil.deleteProfileImage(memberId);

        // 2) DB에서 null 처리
        memberRepository.updateProfileImageUrl(memberId, null);
        log.info("사용자 {} 프로필 이미지 삭제 완료", memberId);
    }

    /**
     * 프로필 이미지 조회
     */
    @Transactional(readOnly = true)
    public String getProfileImageUrl(Long memberId) {
        return memberRepository.findById(memberId)
                .map(m -> {
                    if (m.getProfileImageUrl() != null) {
                        return m.getProfileImageUrl();
                    }
                    return "/default.png"; // 프론트엔드 public 폴더 default.png
                })
                .orElse("/default.png");
    }
}
