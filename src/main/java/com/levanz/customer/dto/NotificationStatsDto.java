package com.levanz.customer.dto;

import com.levanz.customer.entity.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationStatsDto {
    private NotificationStatus status;
    private long count;
}