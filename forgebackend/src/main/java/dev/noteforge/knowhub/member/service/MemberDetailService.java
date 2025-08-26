package dev.noteforge.knowhub.member.service;

import dev.noteforge.knowhub.member.domain.Member;
import dev.noteforge.knowhub.member.repository.MemberRepository;
import dev.noteforge.knowhub.member.security.MemberDetails;
import lombok.RequiredArgsConstructor;
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
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일이 존재하지 않습니다 : " + username));

        System.out.println("get username  " + member.getUsername());

        return new MemberDetails(member);
    }
}
