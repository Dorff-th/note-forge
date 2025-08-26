package dev.noteforge.knowhub.admin.member.mapper;

import dev.noteforge.knowhub.member.dto.MemberDetailDTO;
import dev.noteforge.knowhub.member.dto.MemberResultDTO;
import dev.noteforge.knowhub.member.dto.MemberSearchDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMemberMapper {
    List<MemberResultDTO> searchMembers(MemberSearchDTO dto);

    MemberDetailDTO findById(Long id);

    int updateStatus(@Param("id") Long id,
                     @Param("status") String status);

    int updateDeleted(@Param("id") Long id,
                      @Param("deleted") Boolean deleted);
}
