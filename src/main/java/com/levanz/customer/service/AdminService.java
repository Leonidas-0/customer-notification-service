package com.levanz.customer.service;

import com.levanz.customer.dto.AdminDto;

import java.util.List;

public interface AdminService {
    AdminDto create(AdminDto dto);
    AdminDto update(Long id, AdminDto dto);
    void delete(Long id);
    AdminDto getById(Long id);
    List<AdminDto> getAll();
}
