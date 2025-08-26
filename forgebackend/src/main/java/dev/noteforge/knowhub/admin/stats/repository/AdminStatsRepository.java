package dev.noteforge.knowhub.admin.stats.repository;

import dev.noteforge.knowhub.admin.stats.dto.AdminStatsAllDTO;
import dev.noteforge.knowhub.admin.stats.dto.AdminStatsTodayDTO;
import dev.noteforge.knowhub.member.domain.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface AdminStatsRepository extends Repository<Member, Long> {

    @Query("""
        SELECT new dev.noteforge.knowhub.admin.stats.dto.AdminStatsAllDTO(
            (SELECT COUNT(m) FROM Member m) AS mCount,
            (SELECT COUNT(p) FROM Post p) AS pCount,
            (SELECT COUNT(c) FROM Comment c) as cCount,
            (SELECT COUNT(a) FROM Attachment a WHERE a.uploadType = 'ATTACHMENT') AS attCount,
            (SELECT COUNT(a) FROM Attachment a WHERE a.uploadType = 'EDITOR_IMAGE') AS emCount,
            (SELECT COUNT(ca) FROM Category ca),
            (SELECT COUNT(t) FROM Tag t)
        )
        FROM Member m
        WHERE m.id = 23
    """)
    AdminStatsAllDTO getAllStats();

    @Query("""
        SELECT new dev.noteforge.knowhub.admin.stats.dto.AdminStatsTodayDTO(
            (SELECT COUNT(m) FROM Member m WHERE DATE(m.createdAt) = CURRENT_DATE),
            (SELECT COUNT(p) FROM Post p WHERE DATE(p.createdAt) = CURRENT_DATE),
            (SELECT COUNT(c) FROM Comment c WHERE DATE(c.createdAt) = CURRENT_DATE)
        )
        FROM Member m
        WHERE m.id = 23
    """)
    AdminStatsTodayDTO getTodayStats();
}
