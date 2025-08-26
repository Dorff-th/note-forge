package dev.noteforge.knowhub.admin.service;

import dev.noteforge.knowhub.admin.member.service.AdminMemberService;
import dev.noteforge.knowhub.member.dto.MemberDetailDTO;
import dev.noteforge.knowhub.member.dto.MemberResultDTO;
import dev.noteforge.knowhub.member.dto.MemberSearchDTO;
import dev.noteforge.knowhub.admin.member.mapper.AdminMemberMapper;
import dev.noteforge.knowhub.member.enums.MemberStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")   // application-test.yml 사용 시
@Transactional
class AdminMemberServiceTest {

    @Autowired
    private AdminMemberService adminMemberService;

    @Autowired
    private AdminMemberMapper adminMemberMapper;

    @Test
    @DisplayName("활성 사용자 목록 조회")
    void testSearchActiveMembers() {
        // given
        MemberSearchDTO dto = new MemberSearchDTO();
        dto.setTab("active");
        dto.setRole("USER");

        // when
        List<MemberResultDTO> result = adminMemberService.getMembers(dto);

        result.forEach(m->System.out.println(m));


        // then
        assertThat(result).isNotNull();
        assertThat(result).allMatch(m -> m.getStatus() == MemberStatus.ACTIVE && m.isDeleted() == false);
    }

    @Test
    @DisplayName("비활성/탈퇴 사용자 목록 조회")
    void testSearchInactiveMembers() {
        // given
        MemberSearchDTO dto = new MemberSearchDTO();
        dto.setTab("inactive");

        // when
        List<MemberResultDTO> result = adminMemberService.getMembers(dto);

        result.forEach(m->System.out.println(m));

        // then
        assertThat(result).isNotNull();
        assertThat(result).allMatch(m -> m.getStatus() == MemberStatus.INACTIVE || m.isDeleted() == true);
    }

    @Test
    @DisplayName("회원 상세 조회 성공")
    void getMemberDetail() {
        // given : 테스트용 회원이 DB에 있다고 가정 (id=1)
        Long memberId = 28L;

        // when
        MemberDetailDTO detail = adminMemberService.getMemberDetail(memberId);

        System.out.println(detail);

        // then
        assertThat(detail).isNotNull();
        assertThat(detail.getId()).isEqualTo(memberId);
        assertThat(detail.getUsername()).isNotBlank();
    }

    @Test
    @DisplayName("회원 상태 변경 성공")
    void updateStatus() {
        // given
        Long memberId = 28L;
        String newStatus = "ACTIVE";

        // when
        adminMemberService.updateStatus(memberId, newStatus);
        MemberDetailDTO detail = adminMemberService.getMemberDetail(memberId);

        System.out.println(detail);

        // then
        assertThat(detail.getStatus()).isEqualTo(newStatus);
    }

    @Test
    @DisplayName("회원 탈퇴 여부 변경 성공")
    void updateDeleted() {
        // given
        Long memberId = 28L;
        Boolean newDeleted = true;

        // when
        adminMemberService.updateDeleted(memberId, newDeleted);
        MemberDetailDTO detail = adminMemberService.getMemberDetail(memberId);

        // then
        assertThat(detail.getDeleted()).isTrue();
    }

    @Test
    @DisplayName("테스트 중에는 업데이트가 보이지만, 테스트 끝나면 롤백된다")
    void rollbackExample() {
        Long memberId = 28L;

        // 1. 기존 상태 확인
        MemberDetailDTO before = adminMemberService.getMemberDetail(memberId);
        System.out.println("🔹 BEFORE status = " + before.getStatus());

        // 2. 상태 업데이트 (ACTIVE로 변경)
        adminMemberService.updateStatus(memberId, "ACTIVE");

        // 3. 같은 트랜잭션 안에서 조회하면 바뀐 값이 보인다
        MemberDetailDTO after = adminMemberService.getMemberDetail(memberId);
        System.out.println("🔹 AFTER (inside transaction) status = " + after.getStatus());
        assertThat(after.getStatus()).isEqualTo("ACTIVE");
    }
}
