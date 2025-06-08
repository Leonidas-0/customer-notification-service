package com.levanz.customer.service;

import com.levanz.customer.dto.PreferenceRequestDto;
import com.levanz.customer.dto.PreferenceResponseDto;
import java.util.List;

public interface PreferenceService {
    PreferenceResponseDto create(Long customerId, PreferenceRequestDto dto);
    List<PreferenceResponseDto> listByCustomer(Long customerId);
    PreferenceResponseDto update(Long customerId, Long prefId, PreferenceRequestDto dto);
    void delete(Long customerId, Long prefId);
}