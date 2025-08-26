package dev.noteforge.knowhub.admin.stats.controller;

import dev.noteforge.knowhub.admin.stats.service.AdminStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/stats")
public class AdminStatsController {

    private final AdminStatsService service;

    @GetMapping
    public Object getStats(@RequestParam(defaultValue = "all") String scope) {
        if ("today".equalsIgnoreCase(scope)) {
            return service.getTodayStats();
        }
        return service.getAllStats();
    }
}
