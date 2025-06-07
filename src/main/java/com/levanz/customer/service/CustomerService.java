package com.levanz.customer.service;

import com.levanz.customer.dto.CustomerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    CustomerDto create(CustomerDto dto);

    CustomerDto update(Long id, CustomerDto dto);

    void delete(Long id);

    CustomerDto one(Long id);

    Page<CustomerDto> search(String q, Pageable pageable);
}
