package com.levanz.customer.service;

import com.levanz.customer.dto.CustomerRequestDto;
import com.levanz.customer.dto.CustomerResponseDto;
import com.levanz.customer.entity.Customer;
import com.levanz.customer.mapper.CustomerMapper;
import com.levanz.customer.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repo;
    private final CustomerMapper mapper;

    public CustomerServiceImpl(
        CustomerRepository repo,
        CustomerMapper mapper
    ) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public CustomerResponseDto create(CustomerRequestDto dto) {
        Customer entity = mapper.toEntity(dto);
        Customer saved = repo.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public CustomerResponseDto update(Long id, CustomerRequestDto dto) {
        Customer existing = repo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Customer not found: " + id
            ));
        mapper.updateEntity(dto, existing);
        Customer updated = repo.save(existing);
        return mapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(
              HttpStatus.NOT_FOUND, "Customer not found: " + id
            );
        }
        repo.deleteById(id);
    }

    @Override
    public CustomerResponseDto one(Long id) {
        Customer c = repo.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Customer not found: " + id
            ));
        return mapper.toDto(c);
    }

    @Override
    public Page<CustomerResponseDto> search(String q, Pageable pageable) {
        return repo
            .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(q, q, pageable)
            .map(mapper::toDto);
    }
}
