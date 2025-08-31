package dev.noteforge.knowhub.member.service;

import dev.noteforge.knowhub.member.domain.Member;
import dev.noteforge.knowhub.member.enums.MemberStatus;
import dev.noteforge.knowhub.member.repository.MemberRepository;
import dev.noteforge.knowhub.member.security.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
목적: Spring Security가 인증 시 사용하는 "사용자 정보 조회용" 서비스
| 구분     | 설명                                                                                   |
| ------ | ------------------------------------------------------------------------------------ |
| 핵심 역할  | `UserDetailsService`를 구현해 Spring Security가 로그인 시 `Member` 조회할 수 있도록 연결               |
| 인터페이스  | `org.springframework.security.core.userdetails.UserDetailsService`                   |
| 필수 메서드 | `UserDetails loadUserByUsername(String username)`                                    |
| 사용 위치  | Spring Security 내부에서 호출됨 (`SecurityFilterChain` 설정에서 `.userDetailsService(...)`로 등록) |

 */

@RequiredArgsConstructor
@Service
public class MemberDetailService implements UserDetailsService  {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        //System.out.println("get username  " + member.getUsername());

        // 🚨 비활성 회원 차단
        if (member.getStatus() == MemberStatus.INACTIVE) {
            throw new DisabledException("비활성화된 회원입니다.");
        }

        // 🚨 탈퇴 회원 차단
        if (member.isDeleted()) {
            throw new DisabledException("탈퇴된 회원입니다.");
        }

        return new MemberDetails(member);
    }
}
