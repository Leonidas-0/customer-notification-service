package com.levanz.customer.service;

import com.levanz.customer.dto.CustomerDto;
import com.levanz.customer.entity.Customer;
import com.levanz.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repo;

    /* ---------- CRUD ---------- */

    @Override
    public CustomerDto create(CustomerDto dto) {
        Customer saved = repo.save(toEntity(dto));
        return toDto(saved);
    }

    @Override
    public CustomerDto one(Long id) {
        return toDto(findOrThrow(id));
    }

    @Override
    public CustomerDto update(Long id, CustomerDto dto) {
        Customer existing = findOrThrow(id);
        copy(dto, existing);
        return toDto(repo.save(existing));
    }

    @Override
    public void delete(Long id) {
        repo.delete(findOrThrow(id));
    }

    @Override
    public Page<CustomerDto> search(String q, Pageable p) {
        // simple example – adjust to your needs
        return repo.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(q, q, p)
                   .map(this::toDto);
    }

    /* ---------- helpers ---------- */

    private Customer findOrThrow(Long id) {
        return repo.findById(id)
                   .orElseThrow(() -> new ResponseStatusException(
                           NOT_FOUND, "Customer not found: " + id));
    }

    /* Entity ↔ DTO conversions */

    private Customer toEntity(CustomerDto dto) {
        Customer c = new Customer();
        copy(dto, c);
        return c;
    }

    private CustomerDto toDto(Customer c) {
        CustomerDto dto = new CustomerDto();
        dto.setId(c.getId());
        dto.setFirstName(c.getFirstName());
        dto.setLastName(c.getLastName());
        dto.setEmail(c.getEmail());
        return dto;
    }

    private void copy(CustomerDto src, Customer target) {
        target.setFirstName(src.getFirstName());
        target.setLastName(src.getLastName());
        target.setEmail(src.getEmail());
    }
}
