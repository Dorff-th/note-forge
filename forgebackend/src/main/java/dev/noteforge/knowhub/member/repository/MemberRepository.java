package dev.noteforge.knowhub.member.repository;

import dev.noteforge.knowhub.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository  extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    //email 중복체크
    boolean existsByEmail(String email);

    //nickname 중복체크
    boolean existsByNickname(String nickname);
}
