package com.levanz.customer.service;

import com.levanz.customer.dto.NotificationRequestDto;
import com.levanz.customer.dto.NotificationResponseDto;
import com.levanz.customer.entity.NotificationStatus;
import java.util.List;

public interface NotificationService {
    NotificationResponseDto create(Long customerId, NotificationRequestDto dto);
    List<NotificationResponseDto> listByCustomer(Long customerId);
    List<NotificationResponseDto> listByStatus(NotificationStatus status);
    NotificationResponseDto update(Long customerId, Long notifId, NotificationRequestDto dto);
    void delete(Long customerId, Long notifId);
}