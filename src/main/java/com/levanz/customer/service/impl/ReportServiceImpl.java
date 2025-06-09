package com.levanz.customer.service.impl;

import com.levanz.customer.dto.*;
import com.levanz.customer.repository.*;
import com.levanz.customer.service.ReportService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final PreferenceRepository prefRepo;
    private final NotificationRepository notifRepo;

    public ReportServiceImpl(
        PreferenceRepository prefRepo,
        NotificationRepository notifRepo
    ) {
        this.prefRepo = prefRepo;
        this.notifRepo = notifRepo;
    }

    @Override
    public List<PreferenceStatsDto> preferenceStats() {
        return prefRepo.findAll().stream()
            .collect(Collectors.groupingBy(
               p -> p.getChannel(), Collectors.counting()))
            .entrySet().stream()
            .map(e -> new PreferenceStatsDto(e.getKey(), e.getValue()))
            .collect(Collectors.toList());
    }

    @Override
    public List<NotificationStatsDto> notificationStats() {
        return notifRepo.findAll().stream()
            .collect(Collectors.groupingBy(
               n -> n.getStatus(), Collectors.counting()))
            .entrySet().stream()
            .map(e -> new NotificationStatsDto(e.getKey(), e.getValue()))
            .collect(Collectors.toList());
    }
}
