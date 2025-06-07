package com.levanz.customer.service;

import com.levanz.customer.dto.CustomerRequest;
import com.levanz.customer.dto.CustomerResponse;
import com.levanz.customer.entity.Customer;
import com.levanz.customer.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {

    private final CustomerRepository repo;

    /* ---------- Mapping helpers ---------- */
    private Customer toEntity(CustomerRequest r) {
        Customer c = new Customer();
        c.setFirstName(r.firstName());
        c.setLastName(r.lastName());
        c.setPrimaryEmail(r.primaryEmail());
        return c;
    }

    private CustomerResponse toDto(Customer c) {
        return new CustomerResponse(
                c.getId(),
                c.getFirstName(),
                c.getLastName(),
                c.getPrimaryEmail(),
                c.getCreatedAt()
        );
    }

    /* ---------- CRUD ---------- */
    public CustomerResponse create(CustomerRequest req) {
        if (repo.existsByPrimaryEmail(req.primaryEmail()))
            throw new IllegalArgumentException("email already used");

        Customer saved = repo.save(toEntity(req));
        return toDto(saved);
    }

    public CustomerResponse one(Long id) {
        return repo.findById(id)
                   .map(this::toDto)
                   .orElseThrow(() -> new EntityNotFoundException("customer " + id));
    }

    public CustomerResponse update(Long id, CustomerRequest req) {
        Customer c = repo.findById(id)
                         .orElseThrow(() -> new EntityNotFoundException("customer " + id));

        c.setFirstName(req.firstName());
        c.setLastName(req.lastName());
        c.setPrimaryEmail(req.primaryEmail());
        return toDto(c);
    }

    public void delete(Long id) { repo.deleteById(id); }

    /* ---------- Search / list ---------- */
    public Page<CustomerResponse> search(String q, Pageable pageable) {
        Specification<Customer> spec = (root, cq, cb) -> {
            String w = "%" + q.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("firstName")), w),
                    cb.like(cb.lower(root.get("lastName")), w),
                    cb.like(cb.lower(root.get("primaryEmail")), w)
            );
        };
        return repo.findAll(spec, pageable).map(this::toDto);
    }
}
