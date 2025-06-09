package com.levanz.customer.service;

import com.levanz.customer.dto.*;
import java.util.List;

public interface ReportService {
    List<PreferenceStatsDto> preferenceStats();
    List<NotificationStatsDto> notificationStats();
}