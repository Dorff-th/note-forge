package dev.noteforge.knowhub.member.service;

import dev.noteforge.knowhub.common.enums.RoleType;
import dev.noteforge.knowhub.common.exception.DuplicateResourceException;
import dev.noteforge.knowhub.member.domain.Member;
import dev.noteforge.knowhub.member.dto.MemberProfileResponse;
import dev.noteforge.knowhub.member.dto.RegisterRequestDTO;
import dev.noteforge.knowhub.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 회원가입 - 이메일 중복체크, 닉네임 중복체크
 * 회원가입, 탈퇴 처리등을 담당하는 Service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final MemberRepository memberRepository;

    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    };

    public boolean existsByNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    };

    //회원가입 처리
    public void register(RegisterRequestDTO dto) {

        //이메일과 닉네임 중복을 다시한번 확인한다.
        if(memberRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Duplicate.email");
        }

        if(memberRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Duplicate.nickname");
        }

        String encrypted = bCryptPasswordEncoder.encode(dto.getPassword());

        Member member = new Member(dto.getEmail(), encrypted, RoleType.USER, dto.getNickname());

        memberRepository.save(member);
    }

    //나의 정보 조회
    // 👇 username을 Controller에서 받아오도록 변경
    public MemberProfileResponse getMyProfile(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return MemberProfileResponse.builder()
                .profileImageUrl(member.getProfileImageUrl()) // null 허용
                .username(member.getUsername())
                .nickname(member.getNickname())
                .role(member.getRole())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}
