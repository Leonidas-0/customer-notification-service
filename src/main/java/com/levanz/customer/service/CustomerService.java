package com.levanz.customer.service;

import com.levanz.customer.dto.CustomerRequestDto;
import com.levanz.customer.dto.CustomerResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    CustomerResponseDto create(CustomerRequestDto dto);
    CustomerResponseDto update(Long id, CustomerRequestDto dto);
    void delete(Long id);
    CustomerResponseDto one(Long id);
    Page<CustomerResponseDto> search(String q, Pageable pageable);
}
