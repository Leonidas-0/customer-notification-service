package com.levanz.customer.service;

import com.levanz.customer.dto.CustomerRequestDto;
import com.levanz.customer.dto.CustomerResponseDto;
import com.levanz.customer.dto.CustomerSearchCriteriaDto;
import com.levanz.customer.dto.CustomerUpdateDto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {
    CustomerResponseDto create(CustomerRequestDto dto);
    CustomerResponseDto update(Long id, CustomerRequestDto dto);
    void delete(Long id);
    CustomerResponseDto one(Long id);
    Page<CustomerResponseDto> search(String q, Pageable pageable);
    Page<CustomerResponseDto> searchAdvanced(CustomerSearchCriteriaDto criteria, Pageable pageable);
    List<CustomerResponseDto> batchUpdate(List<CustomerUpdateDto> updates);

}
