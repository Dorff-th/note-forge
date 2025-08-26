package dev.noteforge.knowhub.admin.service;

import dev.noteforge.knowhub.admin.stats.service.AdminStatsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AdminStatsServiceTest {

    @Autowired
    private AdminStatsService adminStatsService;

    @DisplayName("관리자기능의 전체통계 조회")
    @Test
    void testAllStat() {
        System.out.println(adminStatsService.getAllStats());
    }

}