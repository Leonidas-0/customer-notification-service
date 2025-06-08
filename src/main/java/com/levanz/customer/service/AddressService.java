package com.levanz.customer.service;

import com.levanz.customer.dto.AddressRequestDto;
import com.levanz.customer.dto.AddressResponseDto;

import java.util.List;

public interface AddressService {
    AddressResponseDto create(Long customerId, AddressRequestDto dto);
    List<AddressResponseDto> listByCustomer(Long customerId);
    AddressResponseDto update(Long customerId, Long addressId, AddressRequestDto dto);
    void delete(Long customerId, Long addressId);
}