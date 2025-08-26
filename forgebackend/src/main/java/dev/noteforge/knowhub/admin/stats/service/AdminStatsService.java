package dev.noteforge.knowhub.admin.stats.service;

import dev.noteforge.knowhub.admin.stats.dto.AdminStatsAllDTO;
import dev.noteforge.knowhub.admin.stats.dto.AdminStatsTodayDTO;
import dev.noteforge.knowhub.admin.stats.repository.AdminStatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminStatsService {

    private final AdminStatsRepository repo;

    public AdminStatsAllDTO getAllStats() {
        return repo.getAllStats();
    }

    public AdminStatsTodayDTO getTodayStats() {
        return repo.getTodayStats();
    }
}
