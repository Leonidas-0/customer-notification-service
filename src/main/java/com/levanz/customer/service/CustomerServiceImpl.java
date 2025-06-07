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
    private final CustomerMapper mapper;   // MapStruct or manual mapper

    /* ─── Create ─────────────────────────────────────────────── */

    @Override
    public CustomerDto create(CustomerDto dto) {
        Customer saved = repo.save(mapper.toEntity(dto));
        return mapper.toDto(saved);
    }

    /* ─── Read ───────────────────────────────────────────────── */

    @Override
    public CustomerDto one(Long id) {
        return repo.findById(id)
                   .map(mapper::toDto)
                   .orElseThrow(() -> new ResponseStatusException(
                           NOT_FOUND, "Customer " + id + " not found"));
    }

    @Override
    public Page<CustomerDto> search(String q, Pageable p) {
        return repo.search(q, p)           // <- your custom query method
                   .map(mapper::toDto);
    }

    /* ─── Update ────────────────────────────────────────────── */

    @Override
    public CustomerDto update(Long id, CustomerDto dto) {
        Customer existing = repo.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(
                                        NOT_FOUND, "Customer " + id + " not found"));

        mapper.updateEntity(dto, existing);   // copy non-null fields
        return mapper.toDto(repo.save(existing));
    }

    /* ─── Delete ────────────────────────────────────────────── */

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(NOT_FOUND, "Customer " + id + " not found");
        }
        repo.deleteById(id);
    }
}
