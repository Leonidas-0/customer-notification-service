package com.levanz.customer.controller;

import com.levanz.customer.dto.*;
import com.levanz.customer.service.ReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService service;

    public ReportController(ReportService service) {
        this.service = service;
    }

    @GetMapping("/preferences")
    public List<PreferenceStatsDto> prefs() {
        return service.preferenceStats();
    }

    @GetMapping("/notifications")
    public List<NotificationStatsDto> notifs() {
        return service.notificationStats();
    }
}
