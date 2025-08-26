package dev.noteforge.knowhub.admin.member.service;

import dev.noteforge.knowhub.member.dto.MemberDetailDTO;
import dev.noteforge.knowhub.member.dto.MemberResultDTO;
import dev.noteforge.knowhub.member.dto.MemberSearchDTO;
import dev.noteforge.knowhub.admin.member.mapper.AdminMemberMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 사용자관리 Service
 */
@Service
@RequiredArgsConstructor
public class AdminMemberService {
    private final AdminMemberMapper adminMemberMapper;

    public List<MemberResultDTO> getMembers(MemberSearchDTO dto) {
        return adminMemberMapper.searchMembers(dto);
    }

    public MemberDetailDTO getMemberDetail(Long id) {
        MemberDetailDTO dto = adminMemberMapper.findById(id);
        if (dto == null) {
            throw new EntityNotFoundException("회원이 존재하지 않습니다. id=" + id);
        }
        return dto;
    }

    public void updateStatus(Long id, String status) {
        int updated = adminMemberMapper.updateStatus(id, status);
        if (updated == 0) {
            throw new EntityNotFoundException("회원 상태 업데이트 실패. id=" + id);
        }
    }

    public void updateDeleted(Long id, Boolean deleted) {
        int updated = adminMemberMapper.updateDeleted(id, deleted);
        if (updated == 0) {
            throw new EntityNotFoundException("회원 탈퇴여부 업데이트 실패. id=" + id);
        }
    }
}
