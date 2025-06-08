package com.levanz.customer.service.impl;

import com.levanz.customer.dto.AddressRequestDto;
import com.levanz.customer.dto.AddressResponseDto;
import com.levanz.customer.entity.Address;
import com.levanz.customer.entity.Customer;
import com.levanz.customer.mapper.AddressMapper;
import com.levanz.customer.repository.AddressRepository;
import com.levanz.customer.repository.CustomerRepository;
import com.levanz.customer.service.AddressService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepo;
    private final CustomerRepository customerRepo;
    private final AddressMapper mapper;

    public AddressServiceImpl(
        AddressRepository addressRepo,
        CustomerRepository customerRepo,
        AddressMapper mapper
    ) {
        this.addressRepo = addressRepo;
        this.customerRepo = customerRepo;
        this.mapper = mapper;
    }

    @Override
    public AddressResponseDto create(Long customerId, AddressRequestDto dto) {
        Customer customer = customerRepo.findById(customerId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Customer not found: " + customerId
            ));

        Address entity = mapper.toEntity(dto);
        entity.setCustomer(customer);
        Address saved = addressRepo.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public List<AddressResponseDto> listByCustomer(Long customerId) {
        if (!customerRepo.existsById(customerId)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Customer not found: " + customerId
            );
        }
        return addressRepo.findByCustomerId(customerId).stream()
                          .map(mapper::toDto)
                          .collect(Collectors.toList());
    }

    @Override
    public AddressResponseDto update(Long customerId, Long addressId, AddressRequestDto dto) {
        Customer customer = customerRepo.findById(customerId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Customer not found: " + customerId
            ));
        Address existing = addressRepo.findById(addressId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Address not found: " + addressId
            ));
        if (!existing.getCustomer().getId().equals(customer.getId())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Address does not belong to customer " + customerId
            );
        }

        mapper.updateEntity(dto, existing);
        Address updated = addressRepo.save(existing);
        return mapper.toDto(updated);
    }

    @Override
    public void delete(Long customerId, Long addressId) {
        Address existing = addressRepo.findById(addressId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Address not found: " + addressId
            ));
        if (!existing.getCustomer().getId().equals(customerId)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Address does not belong to customer " + customerId
            );
        }
        addressRepo.delete(existing);
    }
}
