package dev.noteforge.knowhub.member.repository;

import dev.noteforge.knowhub.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository  extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    //email 중복체크
    boolean existsByEmail(String email);

    //nickname 중복체크
    boolean existsByNickname(String nickname);

    // 특정 회원의 프로필 이미지 URL 업데이트
    @Modifying
    @Query("UPDATE Member m SET m.profileImageUrl = :profileImageUrl WHERE m.id = :memberId")
    int updateProfileImageUrl(@Param("memberId") Long memberId,
                              @Param("profileImageUrl") String profileImageUrl);
}
